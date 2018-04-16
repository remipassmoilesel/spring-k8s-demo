FROM alpine:3.6

RUN adduser tomcat -D

RUN apk update && apk add gnupg openjdk8

COPY app/build/libs/spring-k8s-demo-0.0.1-SNAPSHOT.jar /spring-k8s-demo.jar

COPY docker/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

RUN mkdir /tomcat-logs
RUN chown tomcat:tomcat /tomcat-logs

USER tomcat

EXPOSE 8085
ENTRYPOINT /entrypoint.sh



