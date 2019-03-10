FROM alpine:3.6

RUN adduser tomcat -D

RUN apk update && apk add openjdk8 bash

RUN mkdir /tomcat-logs
RUN chown tomcat:tomcat /tomcat-logs



