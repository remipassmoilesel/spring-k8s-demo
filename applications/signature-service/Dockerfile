FROM docker.remi-pace.fr/k8sdemo.base-image:0.1

RUN apk update && apk add gnupg

COPY build/libs/signature-service-0.0.1-SNAPSHOT.jar /signature-service.jar

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

USER tomcat

EXPOSE 8085
ENTRYPOINT /docker-entrypoint.sh
