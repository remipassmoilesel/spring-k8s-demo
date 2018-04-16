FROM alpine:3.6

RUN adduser spring -D

RUN apk update && apk add gnupg openjdk8

COPY app/build/libs/spring-k8s-demo-0.0.1-SNAPSHOT.jar /spring-k8s-demo.jar

COPY docker/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

USER spring

EXPOSE 8085
ENTRYPOINT /entrypoint.sh



