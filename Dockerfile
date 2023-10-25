FROM maven:3.9.5-amazoncorretto-11 AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests

FROM openjdk:11
COPY --from=builder  /app/target/service_a.jar service_a.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","service_a.jar"]