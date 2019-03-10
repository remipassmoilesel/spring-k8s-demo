FROM registry.gitlab.com/remipassmoilesel/spring-k8s-demo/k8sdemo.base-image:0.1

RUN apk update && apk add gnupg

COPY build/libs/signature-service-0.0.1-SNAPSHOT.jar /signature-service.jar

USER tomcat

EXPOSE 8081
CMD java -jar /signature-service.jar
