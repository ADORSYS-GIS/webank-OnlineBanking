# Run stage
FROM openjdk:17-jdk-slim

WORKDIR /webank-OnlineBanking

# py the JAR file from the online-banking-app module
COPY ./online-banking-app/target/online-banking-app-0.1-SNAPSHOT.jar /webank-OnlineBanking/online-banking-app-0.1-SNAPSHOT.jar
# Expose the port your app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/webank-OnlineBanking/online-banking-app-0.1-SNAPSHOT.jar"]