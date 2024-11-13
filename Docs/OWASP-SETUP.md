# OWASP Dependency-Check Integration in Multi-Module Maven Project

## Overview

This document provides the configuration and setup details for integrating **OWASP Dependency-Check** into a multi-module Maven project. The goal is to ensure that **all modules** within the project are properly scanned for vulnerabilities in their dependencies.

## Prerequisites

- Maven 3.x or higher
- OWASP Dependency-Check plugin (version `11.1.0` or newer)
- A multi-module Maven project setup

## Project Structure

The project has the following directory structure:

root-pom.xml  
├── obs  
│ └── pom.xml  
├── online-banking-app  
│ └── pom.xml  
└── target

### Root `pom.xml`

The parent POM (`root-pom.xml`) contains the common configurations and plugin definitions shared by all modules in the project. The OWASP Dependency-Check plugin is configured in this POM so that it can be inherited by child modules.

## OWASP Dependency-Check Plugin Configuration

### Parent `pom.xml` (root-pom.xml)

In the root `pom.xml`, define the OWASP Dependency-Check plugin in the `<build>` section to ensure it is inherited by all child modules:

```xml
<build>
    <plugins>
        <!-- OWASP Dependency-Check Plugin -->
        <plugin>
            <groupId>org.owasp</groupId>
            <artifactId>dependency-check-maven</artifactId>
            <version>${owasp.dependency.check.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>check</goal>
                        <goal>update-only</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <aggregate>true</aggregate>
                <failBuildOnCVSS>0</failBuildOnCVSS>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Running the Plugin
Once the OWASP Dependency-Check plugin is configured in the root POM, you can run the checks for all modules by executing the following Maven command from the root of the project:
```mvn clean install``` or ```mvn dependency-check:check```

### Viewing the Report

After the build completes, the Dependency-Check plugin will generate a detailed report of any vulnerabilities found in the dependencies. The report will be saved in the following directory:
```target/dependency-check-report``` of each project module

### Troubleshooting

**If you encounter issues where the modules are not being scanned:**

1. Check that the child modules inherit the parent POM correctly.

2. Ensure that the dependency-check-maven plugin version is correct 
(version **11.1.0** in our case case).

3. Verify that the executions block is correctly set up in the root POM.

4. Ensure there are no exclusions or misconfigurations that could prevent the scan from running on a module.


### Regular Database Updates
It is important to keep the OWASP Dependency-Check database up to date. You can configure periodic updates for the vulnerability database using the **update-only** goal:

```mvn dependency-check:update-only```

This command will only update the vulnerability database, ensuring you are scanning with the latest data.

### Conclusion

With this setup, the OWASP Dependency-Check plugin will automatically scan all modules in the multi-module Maven project for known security vulnerabilities in their dependencies. This helps ensure that the project remains secure and that any vulnerabilities are identified early.