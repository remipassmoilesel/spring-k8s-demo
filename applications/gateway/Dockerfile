FROM alpine:3.6

RUN adduser tomcat -D

RUN apk update && apk add gnupg openjdk8

COPY build/libs/spring-k8s-demo-0.0.1-SNAPSHOT.jar /spring-k8s-demo.jar

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

RUN mkdir /tomcat-logs
RUN chown tomcat:tomcat /tomcat-logs

USER tomcat

EXPOSE 8085
ENTRYPOINT /docker-entrypoint.sh



