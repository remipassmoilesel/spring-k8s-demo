# Spring on Kubernetes !

## Purpose

Show how Spring work like a charm on Kubernetes.

## Description

With this application, you can upload documents, save then sign them with a
GPG key.

This application can be deployed easily on a Kubernetes cluster.

## IntelliJ settings

In order to restart application on build in developement mode, configure Intellij:

     File | Settings | Build, Execution, Deployment | Build Tools | Gradle | Runner
     => Delegate IDE build/run actions to gradle.

## TODO

- Sonarqube analysis ?
- Deployment
- Health check route
- Document download
- Frontend improvements