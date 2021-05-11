
FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD

COPY . .

RUN mvn clean package

FROM openjdk:8-jre-alpine3.9

COPY /target/library-api-1.jar /library-api-1.jar

CMD ["java", "-jar", "library-api-1.jar"]