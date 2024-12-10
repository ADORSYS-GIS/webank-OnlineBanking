# Webank Online Banking System
###  An Online banking system middleware service
## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
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

## Prerequisites
- **Java 11+**: Required to run the Spring Boot applications.
- **Maven**: Used for project dependency management.
- **Docker** (optional): For containerization and easier deployment.
- **PostgreSQL**: As the primary database for storing user and account data.

## Installation Instructions
1. **Clone the Repository**:
    ```bash
    git clone https://github.com/yourusername/webank-online-banking-system.git
    cd webank-online-banking-system
    ```
2. **Run Database Migration**:
    - Ensure PostgreSQL is running and create necessary databases and tables (migration scripts are included in the `db/migrations` folder).

3. **Build and Run Services**:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
4**Access API Documentation**:
    - The OpenAPI documentation will be available at `http://localhost:8080/swagger-ui.html` (or the port specified for each module).

## Starting application with Docker Compose

1. Whilst inside the project root directory, Run the command 
```bash
 docker compose up --build 
 ```
2. Access the API documentation at 
```bash
http://localhost:9200/swagger-ui.html
```
3. you can stop the application with 
```bash
docker compose down
```

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
- Special thanks to the development team and contributors for their support and dedication in building this project.
