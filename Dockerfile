#Docker file

#jdk17 Image Start
FROM openjdk:17
COPY build/libs/writeRoom-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
