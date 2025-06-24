# Webank Online Banking System

### An Online banking system middleware service
## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation Instructions](#installation-instructions)
- [Usage](#usage)
- [Project Documentation](#project-documentation)
- [Development and Contribution](#development-and-contribution)
- [License](#license)
- [Contact Information](#contact-information)
- [Acknowledgements](#acknowledgements)

---

## Project Overview

The **Webank Online Banking System** is a middleware service designed to connect the frontend applications with a core banking system, providing a seamless online banking experience for users. The system consists of various modules to manage key functionalities such as user registration, account management, OTP verification, and access control.

### Key Modules

- **OBS (Online Banking Service)**: Orchestrates requests for registration and OTP verification, forwarding them to the appropriate backend modules.
- **PRS (Personal Registration Service)**: Manages user registration and OTP verification.
- **DAS (Deposit Account Service)**: Handles account creation and balance management.
- **AAS (Account Access Service)**: Manages account access and authorization.
- **SMS Gateway**: Sends OTPs to users for verification purposes.

## Features

- **User Registration**: Users can register by providing their phone numbers and public keys.
- **OTP Verification**: Secure one-time passwords are sent to users’ phones to verify identity.
- **Account Management**: Once registration is complete, users can create bank accounts, check balances, and view transaction histories.
- **Scalable Microservices Architecture**: Each service is designed as an independent microservice, allowing for modularity and scalability.

## Technologies Used

- **Backend**: Spring Boot for the OBS, PRS, DAS, and AAS modules.
- **Database**: PostgreSQL (or any preferred database system) for secure data storage.
- **API Documentation**: OpenAPI for standardized API documentation.
- **Messaging and SMS**: Integrated SMS gateway for OTPs.

## Getting Started

This project supports building native images using GraalVM Native Image for fast startup and low memory usage.

To get up and running quickly:

1. Review the [Prerequisites](#prerequisites) to ensure your environment is ready.
2. Follow the [Installation Instructions](#installation-instructions) for cloning, configuring, and building the project.
3. See the [Usage](#usage) section for how to run and interact with the application.

## Prerequisites

- **Java 17**: Required to run the Spring Boot applications and build native images.
- **SDKMAN**: For managing Java versions and GraalVM.
- **Docker** (optional): For containerization and easier deployment.
- **Maven**: Used for project dependency management.
- **PostgreSQL**: As the primary database for storing user and account data.

## Installation Instructions

1. **Clone the Repository**:

    ```bash
    git clone https://github.com/ADORSYS-GIS/webank-OnlineBanking.git
    cd webank-OnlineBanking
    ```

1. **Install SDKMAN (if not already installed)**:

    ```bash
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"
    ```

1. **Install and Use GraalVM**:

    ```bash
    sdk list java | grep graalvm
    sdk install java 17.0.10-graal
    sdk use java 17.0.10-graal
    gu install native-image
    java -version          # Should show GraalVM
    graalvm-native-image --version  # Should show native-image version
    ```

1. **Enable GraalVM for the project**:

    ```bash
    sdk env install
    sdk use
    ```

1. **Set Required Environment Variables**:

    - `JWT_ISSUER`, `JWT_EXPIRATION_TIME_MS`, `SERVER_PRIVATE_KEY_JSON`, `SERVER_PUBLIC_KEY_JSON`
    - Export these before running the app or building the native image.

1. **Update your Maven `settings.xml` if not yet updated**:

    Add the following to your `~/.m2/settings.xml` to ensure Maven can access private GitHub repositories:

    ```xml
    <settings>
        <servers>
            <server>
                <id>github</id>
                <username>your_github_username</username>
                <password>your_github_token</password>
            </server>
            <server>
                <id>github-webank</id>
                <username>your_github_username</username>
                <password>your_github_token</password>
            </server>
            <server>
                <id>github-ledgers</id>
                <username>your_github_username</username>
                <password>your_github_token</password>
            </server>
        </servers>
    </settings>
    ```

    > Replace `your_github_username` and `your_github_token` with your actual GitHub username and a Personal Access Token with appropriate permissions.

1. **Build and Run Services**:

    - **Native Image (GraalVM):**

    ```bash
    mvn clean package -Pnative -DskipTests
    ./online-banking-app/target/online-banking-app-*
    ```

    - **Standard JVM:**

    ```bash
    mvn clean install
    cd online-banking-app
    mvn spring-boot:run
    ```

1. **Access API Documentation**:

    - The OpenAPI documentation will be available at `http://localhost:8081/swagger-ui.html` (or the port specified for each module).

1. **Using Docker (optional):**

    ```bash
    export GH_USERNAME=your_github_username
    export GH_PASSWORD=your_github_token
    docker build --build-arg GH_USERNAME --build-arg GH_PASSWORD -t webank-app .
    docker run -p 8081:8081 webank-app
    ```

    > **IMPORTANT:** Ensure your Maven `settings.xml` is up to date and contains credentials for all required repositories. The Docker build uses `settings.template.xml` to provision `/root/.m2/settings.xml` for dependency resolution.

---

## Usage

- **User Registration**:
  - Make a `POST` request to `/register` with `phoneNumber` and `publicKey`.
  - Receive an OTP on the provided phone number.
- **OTP Verification**:
  - Verify OTP by making a `POST` request to `/verify-otp` with the OTP and public key.
- **Account Management**:
  - Access endpoints for balance inquiries, transfers, and account details.

For a detailed guide on using each endpoint, refer to the [API Documentation](#project-documentation) section.

## Project Documentation

The architecture documentation and API specifications are available in the `docs` folder. The documentation includes:

- **Architecture Overview**: Detailed information on how OBS interacts with PRS, DAS, and AAS.
- **API Reference**: List of available endpoints, request payloads, and response formats.
- **Security Considerations**: Overview of authentication and data encryption practices.

## Development and Contribution

We welcome contributions! Please follow these steps:

1. Fork the repository and create a new branch for your feature or bug fix.
2. Follow our [contribution guidelines](CONTRIBUTING.md) for best practices.
3. Submit a pull request detailing your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact Information

For questions or support, please reach out to:

- **Email**: [Adorsys](fpo@adorsys.de)
- **GitHub Issues**: [Submit an issue](https://github.com/ADORSYS-GIS/webank/issues)

## Acknowledgements

- Thanks to the adorsys development team for their contributions.
- Special thanks to the contributors and libraries that supported this project.
