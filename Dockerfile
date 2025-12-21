FROM bellsoft/liberica-runtime-container:jre-21-musl AS builder

WORKDIR /opt

ENV SERVER_PORT=8080
ENV ROOT_LOG_LEVEL=INFO
ENV APP_LOG_LEVEL=DEBUG

EXPOSE 8080

COPY target/*.jar /opt/app.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar /opt/app.jar"]