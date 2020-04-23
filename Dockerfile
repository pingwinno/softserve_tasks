FROM openjdk:11
COPY . /usr/src/webapp/tmp
WORKDIR /usr/src/webapp/tmp
RUN ls
RUN ./mvnw package && cd ../ && mv tmp/target/webapp-0.0.1-SNAPSHOT.jar ./webapp.jar && rm -rf tmp/ && rm -rf ~/.m2
CMD ["java", "-jar", "webapp.jar"]
EXPOSE 8080