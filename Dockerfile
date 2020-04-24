FROM openjdk:12-alpine
COPY target/webapp-0.0.1-SNAPSHOT.jar /usr/src/webapp/webapp.jar
WORKDIR /usr/src/webapp
CMD ["java", "-jar", "webapp.jar"]
EXPOSE 8080