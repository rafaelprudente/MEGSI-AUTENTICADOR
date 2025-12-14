FROM bellsoft/liberica-openjre-debian:21

WORKDIR /opt
ENV SERVER_PORT=8080
ENV ROOT_LOG_LEVEL=INFO
ENV APP_LOG_LEVEL=INFO

EXPOSE 8080

COPY target/*.jar /opt/app.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar /opt/app.jar"]