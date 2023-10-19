FROM openjdk:11
ADD target/ServiceA-0.0.1-SNAPSHOT.jar ServiceA.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","ServiceA.jar"]