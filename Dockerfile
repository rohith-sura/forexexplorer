FROM eclipse-temurin:17-jdk-jammy
EXPOSE 8080
COPY /build/libs/forexexplorer-0.0.1-SNAPSHOT.jar forexexplorer.jar
ENTRYPOINT ["java", "-jar", "/forexexplorer.jar"]