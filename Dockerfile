FROM openjdk:21-jdk

WORKDIR /app

COPY out/artifacts/galaxy_food_server_jar/server.jar /app/server.jar

ENTRYPOINT ["java", "-jar", "server.jar"]