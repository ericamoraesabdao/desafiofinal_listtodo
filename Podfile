FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /desafiofinal_listtodo

COPY ./pom.xml ./
RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /desafiofinal_listtodo
COPY --from=build /desafiofinal_listtodo/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]