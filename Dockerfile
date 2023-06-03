FROM bellsoft/liberica-openjdk-alpine-musl:17
VOLUME /tmp

RUN apk update && apk add stress-ng

COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
