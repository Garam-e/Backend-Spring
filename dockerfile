FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/Garam_e_spring-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} garam-server.jar

ENTRYPOINT ["java","-jar","/garam-server.jar"]