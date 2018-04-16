# Spring on Kubernetes !

## Purpose

Show how Spring work like a charm on Kubernetes.

## Application

With this application, you can upload documents, save then sign them with a
GPG key.

## IntelliJ settings

In order to restart application on build in developement mode, configure Intellij:

     File | Settings | Build, Execution, Deployment | Build Tools | Gradle | Runner
     => Delegate IDE build/run actions to gradle.

## TODO

- Cleanup: useless files, code ...
- Deployment
- Health check route
- Document download