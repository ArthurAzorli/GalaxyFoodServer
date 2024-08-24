FROM openjdk:21-jdk

EXPOSE 8080

ADD target/galaxy-food-server.jar galaxy-food-server.jar

ENTRYPOINT ["java", "-jar", "/galaxy-food-server.jar"]