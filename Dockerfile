FROM openjdk:11
EXPOSE 8181
ARG JAR_FILE=./target/tom-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]