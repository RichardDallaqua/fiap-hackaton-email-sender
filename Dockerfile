FROM maven:3.9.2-amazoncorretto-17 AS builder
COPY pom.xml pom.xml
COPY /src /src
RUN mvn clean install
FROM amazoncorretto:17.0.10-al2023-headless
COPY --from=builder /target/relatorio-api-0.0.1-SNAPSHOT.jar relatorio-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/relatorio-api-0.0.1-SNAPSHOT.jar"]