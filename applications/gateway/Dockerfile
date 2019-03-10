FROM registry.gitlab.com/remipassmoilesel/spring-k8s-demo/k8sdemo.base-image:0.1

COPY build/libs/gateway-0.0.1-SNAPSHOT.jar /gateway.jar

USER tomcat

EXPOSE 8080
CMD java -jar /gateway.jar
