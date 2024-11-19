# Integrating Spring Boot with Grafana Dashboard

This guide highlights the key steps for integrating a Spring Boot application with a Grafana dashboard. 

This document only gives a brief overview of what was done at every step to intgrate this technology into our project. For a more comprehensive guide refer to the [Scribe Guide](https://scribehow.com/shared/Integrate_Spring_Boot_with_Grafana_Dashboard__F-HZZjkBSiCKE3c9VD_42w).

## Steps to Integrate

1. **Connect to Grafana Cloud**:
   - Navigate to your Grafana Cloud instance 
   - Select application in the sidebar to connect your Spring-Boot app.

2. **Install Grafana Alloy**:
   - Create an authentication token for Alloy.
   - Configure and test the Alloy connection.

3. **Update Your Codebase**:
   - Add dependencies: `spring-boot-starter-actuator` and `micrometer-registry-prometheus`.
   - Configure `application.properties` to expose all endpoints.
   - Update the Alloy configuration file with the correct Spring Boot endpoint.

4. **Restart Alloy**:
   - Restart the Alloy service to apply changes.

5. **Install and View Dashboards**:
   - Install relevant dashboards and monitor your application using "Spring Boot Statistics."
