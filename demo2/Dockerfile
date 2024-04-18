FROM openjdk:22-jdk as build
RUN mkdir /tmp/app
COPY ./src /tmp/app/src
WORKDIR /tmp/app
RUN javac -d . ./src/org/example/* && jar -cvfe app.jar org.example.App ./org/example/*.class

FROM eclipse-temurin:22-jre-alpine
WORKDIR /app
COPY --from=build /tmp/app/app.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]