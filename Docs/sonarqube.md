# Introduction

This document describes how to integrate and use SonarQube for code quality analysis in our Spring Boot application.

## Overview

SonarQube is a powerful open-source platform designed to improve code quality and security. It analyzes source code to detect potential bugs, vulnerabilities, and code smells. By identifying these issues early in the development process, SonarQube helps developers write cleaner, more reliable, and more secure code.

## Prerequisites

- maven or gradle for sonar plugins and dependencies
- Docker (for local SonarQube setup)
- SonarScanner CLI or SonarQube plugin in your preferred IDE
- GitHub Repository (for CI/CD integration example)
- Ensure your code has unit tests and generates a coverage report (e.g., lcov.info) for SonarQube to read and analyze.

## SonarQube Installation Options

### Using an Organization-Hosted SonarQube Server

If your organization provides a hosted SonarQube server, you can connect directly to it:

1. Log into the Hosted Server:
    - Access the URL provided by your organization (e.g., <https://sonarqube.companydomain.com>).
    - Use your organization credentials to log in.

2. Create a Project in SonarQube:
    - Go to Projects > Create Project.
    - Provide a unique Project Key and Display Name.
    - Complete the project creation wizard.

3. Generate a SonarQube Token:
    - Go to My Account > Security > Generate Token.
    - Copy the token for later use in CI/CD and local analysis.


## Configuration

- sonar-maven-plugin version

```xml
   <sonar-maven-plugin.version>3.7.0.1746</sonar-maven-plugin.version>
   ```

- sonar maven dependency

```xml
            <dependency>
                <groupId>org.sonarsource.scanner.maven</groupId>
                <artifactId>sonar-maven-plugin</artifactId>
                <version>5.0.0.4389</version>
            </dependency>
```

- jacoco plugin for code coverage and report and excluded files.

```xml
        <sonar.core.coveragePlugin>jacoco</sonar.core.coveragePlugin>
        <sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
        <sonar.coverage.jacoco.xmlReportPaths>${project.build.directory}/site/jacoco/jacoco.xml</sonar.coverage.jacoco.xmlReportPaths>
        <sonar.language>java</sonar.language>
        <sonar.coverage.exclusions>
            **/*.xml,
            **/obs-service-api/**/*,
            **/obs-rest-api/**/*,
            **/*Properties*.java
        </sonar.coverage.exclusions>
        <sonar.moduleName>${project.artifactId}</sonar.moduleName>
```

##  GitHub Actions CI/CD Integration

### GitHub Secrets

In your repository settings, add the SonarQube token as a secret:
To create secrets for a repository, you must be the repository owner or have admin access for organization repositories. Here are the steps to create a secret:

1. **Navigate to Your Repository**: On GitHub, go to the main page of your repository.
2. **Access Settings**: Under your repository name, click **Settings**.
3. **Select Secrets**: In the "Security" section of the sidebar, select **Secrets and variables**, then click **Actions**.
4. **Add New Secret**: Click on **New repository secret**.
5. **Enter Secret Details**:
    - In the **Name** field, type a name for your secret , Add `SONAR_TOKEN` with the generated token value.
    - In the **Secret** field, enter the value for your secret (the actual token).
6. **Save Secret**: Click **Add secret**.

### Using Secrets in Your Workflow

Once you have created your secrets, you can reference them in your GitHub Actions workflows using the syntax `${{ secrets.SECRET_NAME }}`. For example:

```yaml
env:
  SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

### GitHub Actions Workflow Example

Create a `.github/workflows/sonarqube-analysis.yml` file in your repository:

Automate code analysis by integrating SonarQube with GitHub Actions. This workflow will run SonarQube analysis on every push and pull request to the main branch.

```yaml
  Sonarqube:
    name: Sonarqube
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0  # Shallow clones should be disabled for a better relevancy of analysis
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: 17
          distribution: 'zulu' # Alternative distribution options are available.
      - name: Cache SonarQube packages
        uses: actions/cache@v4
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar
      - name: Cache Maven packages
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          restore-keys: ${{ runner.os }}-m2

      - name: Set up Maven settings.xml
        run: |
          mkdir -p ~/.m2
          echo "<settings>
                  <servers>
                    <server>
                      <id>github-webank</id>
                      <username>${{ github.actor }}</username>
                      <password>${{ secrets.WEBANK_ACCESS_TOKEN }}</password>
                    </server>
                  </servers>
                </settings>" > ~/.m2/settings.xml

      - name: Build and analyze
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
          SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
        run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=webank-online-banking -Dsonar.projectName='webank-online-banking' -Ddependency-check.skip=true
```

## How it works

### When the workflow is trigered

1. Check Out Code

- Clones the repository to the runner, including full git history for better analysis.

2. Set Up Java

- Installs Java 17 (Zulu OpenJDK), which is required for the Maven build.

3. Cache Dependencies

- SonarQube Cache: Restores or caches SonarQube files to save time in future runs.
- Maven Cache: Restores or caches Maven dependencies based on the pom.xml file.

4. Configure Maven Authentication

- Sets up a custom settings.xml file with credentials to access private Maven repositories.
- Uses GitHub secrets to securely store and provide the credentials.

5. Build and Analyze

- Runs the Maven build process (mvn verify) to compile the code and run tests.
- Executes the SonarQube Maven Plugin to analyze:,Code quality,Security vulnerabilities and Test coverage.

Alternatively,you can also install and run SonarQube locally using Docker.

### Local Installation with Docker

1. Run SonarQube with Docker:

   ```bash
   docker pull sonarqube
   docker run -d --name sonarqube -p 9000:9000 sonarqube

2. Access SonarQube

- Open a browser and navigate to <http://localhost:9000>.
- Default login: admin / admin.

3. Change Default Password:

- Go to My Account > Security and change the admin password for security.

4. Configure Your Project:
Ensure your project is set up to use the SonarQube Maven plugin and Jacoco for code coverage.
Configure the sonar-scanner.properties file with the correct SonarQube server URL (<http://localhost:9000>) and credentials.

5. Run the SonarQube Analysis:
In your project's root directory, run the following Maven command:
Bash
mvn sonar:sonar

6. Access the SonarQube Web Interface:
Open a web browser and navigate to <http://localhost:9000>.

## Next steps

1. Understand the SonarQube report <https://docs.sonarsource.com/sonarqube-server/9.8/user-guide/metric-definitions/>

2. Address Issues:
Prioritize Issues: Focus on high-severity issues like blockers and critical bugs first.
Fix Issues: Implement code changes to resolve the identified issues, such as fixing bugs, removing code smells, and addressing security vulnerabilities.
Commit Changes: Commit the fixed code to your version control system.
Retrigger Analysis: Run a new SonarQube analysis to verify the fixes and identify any new issues.

3. Improve Code Coverage:
Write More Tests: Increase code coverage by writing more unit, integration, and end-to-end tests.
Refactor Code: Refactor complex code sections to make them more testable.
Analyze Test Coverage Reports: Identify areas with low coverage and target them for additional testing.

4. Enforce Coding Standards:
Define Coding Standards: Establish clear coding standards and guidelines for your team.
Configure SonarQube Rules: Configure SonarQube to enforce these standards through custom rules or by using predefined rulesets.
Review Code Regularly: Conduct regular code reviews to ensure adherence to coding standards.

5. Monitor Technical Debt:
Track Technical Debt: Monitor the technical debt metrics provided by SonarQube.
Prioritize Debt Reduction: Plan and prioritize tasks to reduce technical debt, such as refactoring code or removing unnecessary complexity.
Set Debt Reduction Goals: Establish specific goals for reducing technical debt over time.

6. Leverage SonarQube Features:
Explore Advanced Features: Utilize advanced features like code duplication analysis, security hotspots, and code metrics to gain deeper insights into your code.
Integrate with IDEs: Use SonarLint to get real-time feedback on code quality and security issues within your IDE.
Customize Reports: Customize SonarQube reports to focus on specific metrics and issues that are most relevant to your team.
