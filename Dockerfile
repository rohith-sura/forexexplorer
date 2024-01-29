FROM openjdk:17.0-jdk-oracle
LABEL authors="rohithks"
EXPOSE 8080
COPY --from=build /build/libs/forexexplorer-0.0.1-SNAPSHOT.jar forexexplorer.jar

ENTRYPOINT ["java", "-jar", "/forexexplorer.jar"]