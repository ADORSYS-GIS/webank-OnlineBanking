# Java CI with Maven Workflow Documentation

This document provides a high-level overview of the CI/CD pipeline for the `webank-onlinebanking` project using GitHub Actions. The workflow is designed to ensure code quality, security, and readiness for deployment.

## Workflow Overview

The workflow is triggered on the following events:
- Pushes to the `main` branch.
- Pull requests targeting the `main` branch.

### Key Features:
1. Automated build and dependency management.
2. Comprehensive testing, including unit and integration tests.
3. Security scanning for vulnerabilities in dependencies.
4. Code quality analysis using SonarQube.

---

## Stages in the Workflow

### 1. Test
In this stage:
- Unit and integration tests are executed to validate functionality.
- Tests are run using Maven commands, with relevant outputs and logs generated.

### 2. Build
The build stage compiles the project and resolves dependencies. Maven is used for build automation, with caching implemented to speed up subsequent builds. It also prepares a custom `settings.xml` to authenticate with necessary repositories.


### 3. Security Scan
This stage ensures the security of the project by performing:
- A scan for known vulnerabilities in dependencies using OWASP Dependency-Check.
- Generation of a detailed report for identified issues.
- Uploading the security scan report as an artifact for review.

### 4. Code Quality Analysis (SonarQube)
This stage performs static code analysis using SonarQube to:
- Identify code smells, bugs, and security vulnerabilities.
- Provide actionable insights to improve code quality.
- Integrate with the SonarQube server to display detailed metrics and dashboards.

---

## Execution Environment
The workflow runs on the `ubuntu-latest` environment to ensure compatibility and consistency across stages. Java 17 is used as the target runtime environment, with Temurin and Zulu distributions supported.

---

## Outputs
- Test reports summarizing unit and integration test results.
- Build artifacts generated during the build stage.
- Security reports from OWASP Dependency-Check.
- SonarQube analysis reports available on the configured SonarQube instance.

---

## Summary
This CI/CD workflow ensures a robust pipeline for building, testing, securing, and analyzing the `webank-onlinebanking` project. It facilitates automated processes to maintain high code quality and security standards while accelerating development cycles.
