# Configuration Management

This document outlines the configuration management approach used in the Webank Online Banking application.

## Overview

The application uses Spring Boot's configuration capabilities with a focus on:
- Type-safe configuration properties
- Environment-specific configurations
- Secure handling of sensitive data
- Validation of configuration values
- Comprehensive documentation

## Configuration Structure

### Configuration Properties

Configuration properties are defined in Java classes annotated with `@ConfigurationProperties` and validated using Bean Validation annotations.

Key configuration classes:
- `ServerProperties`: Server-specific settings (keys, ports, etc.)
- `JwtProperties`: JWT-related configuration

### Property Files

| File | Purpose |
|------|---------|
| `application.yml` | Base configuration with common settings |
| `application-h2.yml` | H2 database configuration |
| `application-postgres.yml` | PostgreSQL database configuration |

## Environment Variables

Sensitive configuration should be provided via environment variables:

```bash
# Required in all environments
export SERVER_PRIVATE_KEY_JSON='{...}'
export SERVER_PUBLIC_KEY_JSON='{...}'

# Database credentials (for production)
export SPRING_DATASOURCE_USERNAME=dbuser
export SPRING_DATASOURCE_PASSWORD=dbpass
```

## Profiles

The application uses Spring Profiles to switch between database configurations:

- **h2** (default): H2 in-memory database for development
  ```bash
  java -jar app.jar
  # or explicitly
  java -jar app.jar --spring.profiles.active=h2
  ```

- **postgres**: PostgreSQL database
  ```bash
  java -jar app.jar --spring.profiles.active=postgres
  ```

You can also combine profiles if needed:
```bash
java -jar app.jar --spring.profiles.active=h2,debug
```

## Configuration Validation

Configuration properties are validated at startup using Bean Validation annotations. The application will fail to start if required properties are missing or invalid.

## IDE Support

Configuration metadata is provided for IDE support, including:
- Code completion
- Type information
- Documentation
- Validation

## Security Considerations

- Never commit sensitive data to version control
- Use environment variables for secrets
- Follow the principle of least privilege for database users
- Rotate keys and passwords regularly
- Use HTTPS in production

## Best Practices

1. **Keep default values safe**: Defaults should be suitable for development
2. **Document all properties**: Every configuration property should be documented
3. **Validate early**: Fail fast with clear error messages
4. **Use environment variables for secrets**: Never hardcode sensitive information
5. **Keep environment-specific configs separate**: Use profiles for environment differences

## Adding New Configuration

1. Add the property to the appropriate `@ConfigurationProperties` class
2. Add validation annotations as needed
3. Document the property in the class Javadoc
4. Add IDE metadata if needed
5. Update this documentation if the change affects the configuration structure
