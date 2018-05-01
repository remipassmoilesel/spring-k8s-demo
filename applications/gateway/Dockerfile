FROM docker.remi-pace.fr/k8sdemo.base-image:0.1

COPY build/libs/gateway-0.0.1-SNAPSHOT.jar /gateway.jar

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

USER tomcat

EXPOSE 8080
ENTRYPOINT /docker-entrypoint.sh
