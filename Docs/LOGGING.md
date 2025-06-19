# Logging Strategy

This document outlines the logging strategy for the Webank Online Banking application.

## Structured Logging

The application uses structured logging to generate logs in JSON format. This makes the logs easier to parse, search, and analyze, especially in a centralized logging system.

The log format is defined in `src/main/resources/logback-spring.xml` and includes the following fields:

- `severity`: The log level (e.g., INFO, DEBUG, ERROR).
- `service`: The name of the application (`online-banking-app`).
- `trace`: The trace ID for distributed tracing.
- `span`: The span ID for distributed tracing.
- `correlation_id`: A unique ID for each request, used to trace a request through the system.
- `pid`: The process ID of the application.
- `thread`: The name of the thread that generated the log event.
- `class`: The name of the class that generated the log event.
- `message`: The log message.

## Correlation IDs

To facilitate debugging and tracing, every incoming request is assigned a unique correlation ID. This ID is included in every log statement generated during the processing of that request.

The correlation ID is handled by the `CorrelationIdFilter`. If a request includes an `X-Correlation-ID` header, that value is used; otherwise, a new UUID is generated.

The correlation ID is also included in the response headers, so clients can track the request.

## Log Levels

Log levels can be configured for different environments using Spring profiles. The default log level is `INFO`.

- **dev**: The `dev` profile sets the log level to `DEBUG` for more detailed logging during development.
- **prod**: The `prod` profile uses the `INFO` log level to reduce log volume in production.

To activate a specific profile, you can set the `spring.profiles.active` property when running the application.

