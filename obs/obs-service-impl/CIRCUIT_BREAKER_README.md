# Circuit Breaker Implementation with Resilience4j

## Overview
This implementation adds circuit breaker pattern to the `BalanceServiceImpl` using Resilience4j to improve application resilience against external service failures.

## Implementation Details

### 1. Dependencies Added
- `resilience4j-spring-boot3` (v2.2.0)
- `resilience4j-circuitbreaker` (v2.2.0)

### 2. Key Components

#### BalanceResponse DTO
- New response class to handle different status scenarios
- Includes status, message, balance, and accountId fields
- Static factory methods for different response types:
  - `success()` - Normal successful response
  - `serviceUnavailable()` - Circuit breaker fallback response
  - `error()` - Error response

#### Circuit Breaker Configuration
- **Sliding Window**: Count-based (10 calls)
- **Failure Rate Threshold**: 50%
- **Wait Duration**: 60 seconds in open state
- **Half-Open Calls**: 3 permitted calls
- **Minimum Calls**: 5 before calculating failure rate
- **Timeout**: 5 seconds for external service calls

#### BalanceServiceImpl Updates
- Added `@CircuitBreaker` annotation with fallback method
- Enhanced logging for monitoring circuit breaker events
- Proper exception handling to trigger circuit breaker
- Fallback method returns `SERVICE_UNAVAILABLE` status

### 3. Configuration Files

#### application.yml
```yaml
resilience4j:
  circuitbreaker:
    instances:
      balanceService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 60s
        permittedNumberOfCallsInHalfOpenState: 3
        minimumNumberOfCalls: 5
```

#### ResilienceConfig.java
- Programmatic configuration for circuit breaker
- Time limiter configuration for external service calls

### 4. API Changes

#### Before
```java
ResponseEntity<String> getBalance(String authorizationHeader, BalanceRequest request)
```

#### After
```java
ResponseEntity<BalanceResponse> getBalance(String authorizationHeader, BalanceRequest request)
```

### 5. Response Format

#### Success Response
```json
{
  "status": "SUCCESS",
  "message": "Balance retrieved successfully",
  "balance": "1000",
  "accountId": "12345"
}
```

#### Service Unavailable Response (Circuit Breaker)
```json
{
  "status": "SERVICE_UNAVAILABLE",
  "message": "External service is temporarily unavailable",
  "balance": "0",
  "accountId": "12345"
}
```

#### Error Response
```json
{
  "status": "ERROR",
  "message": "Balance empty",
  "balance": "0",
  "accountId": "12345"
}
```

## Testing

### Unit Tests
- `BalanceServiceImplTest` - Updated for new response format
- `BalanceServiceCircuitBreakerTest` - Circuit breaker behavior tests

### Test Scenarios
1. **Normal Operation**: Successful balance retrieval
2. **Empty Balance**: Account with no balance
3. **Service Failure**: External service unavailable
4. **Circuit Breaker Trigger**: Multiple failures triggering fallback
5. **Fallback Method**: Direct fallback method testing

## Monitoring and Logging

### Log Levels
- `DEBUG` for circuit breaker events
- `INFO` for successful operations
- `WARN` for circuit breaker fallbacks
- `ERROR` for service failures

### Health Indicators
- Circuit breaker health indicator registered
- Available via `/actuator/health` endpoint

## Circuit Breaker States

### CLOSED (Normal)
- All calls pass through to external service
- Failures are counted and tracked

### OPEN (Failure)
- Circuit breaker opens after failure threshold
- All calls immediately return fallback response
- No calls reach external service

### HALF-OPEN (Recovery)
- After wait duration, circuit breaker allows limited calls
- If successful, returns to CLOSED state
- If failed, returns to OPEN state

## Benefits

1. **Fault Tolerance**: Prevents cascading failures
2. **Fast Failure**: Quick response when services are down
3. **Automatic Recovery**: Self-healing when services recover
4. **Resource Protection**: Prevents resource exhaustion
5. **Better User Experience**: Graceful degradation

## Usage Example

```java
@CircuitBreaker(name = "balanceService", fallbackMethod = "getBalanceFallback")
public BalanceResponse getBalance(BalanceRequest balanceRequest) {
    // External service call
    // If fails, circuit breaker triggers fallback
}

public BalanceResponse getBalanceFallback(BalanceRequest balanceRequest, Exception exception) {
    // Fallback logic
    return BalanceResponse.serviceUnavailable(balanceRequest.getAccountID());
}
```

## Future Enhancements

1. **Metrics**: Add Micrometer metrics for monitoring
2. **Dashboard**: Circuit breaker state visualization
3. **Configuration**: External configuration management
4. **Multiple Services**: Extend to other external services
5. **Retry Logic**: Add retry mechanism before circuit breaker 