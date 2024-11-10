FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dspring.profiles.active=default

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*-SNAPSHOT.jar ./app.jar
COPY bin/healthcheck.sh ./
RUN chown root:root healthcheck.sh && \
    chmod +x healthcheck.sh
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
