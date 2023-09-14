#
# Build stage
#
#FROM maven:3.8.3-openjdk-17 AS build
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean package
#
#
# Package stage
#
FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]