FROM maven:3.9.16-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /workspace/target/task-api-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:tasks SPRING_DATASOURCE_USERNAME=sa SPRING_DATASOURCE_PASSWORD=
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]