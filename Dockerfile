#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build

COPY . /apps/
COPY pom.xml /apps/
WORKDIR /apps

RUN mvn install

#
# Package stage
#
FROM tomcat:latest

COPY --from=build apps/target/SimpleApp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]