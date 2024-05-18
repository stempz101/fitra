FROM eclipse-temurin:17
WORKDIR /app
COPY target/fitra-*.jar fitra.jar
EXPOSE 8081:8081
ENTRYPOINT ["java", "-jar", "fitra.jar"]
