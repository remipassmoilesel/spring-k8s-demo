# Spring on Kubernetes !

## Purpose

This is a small application built with Spring Boot and VueJS, intended to be deployed on a Kubernetes cluster. 
With this application, you can upload documents, sign them then check them with a GPG key. 

## Why Kubernetes ?

Kubernetes offer many advantages:

- With kubernetes and Docker, deploying an application is really easy
- You can deploy any type of technology, including technologies that are not normally compatible with each other
- Applications are overseen and responsive: they heal themselves, and they can scale according to their activities
- The use of servers is optimized: no RAM or CPU that sleep and cost money

## Use and test application

/!\ Warning: This application has only been tested on Ubuntu and Alpine Linux

You can try this application locally easily. You will need NodeJS 8, npm, and a Mariadb/MySQL database:

```
    $ sudo apt install -y mariadb-server nodejs npm
    $ sudo systemctl start mysql
```

Then clone and setup the database:

```
    $ git clone https://github.com/remipassmoilesel/spring-k8s-demo
    $ cd spring-k8s-demo
    $ ./scripts/setup-dev-db.sh
```

Then build and run the application locally:

```
    $ ./scripts/package*app.sh
    $ ./scripts/launch-dev.sh
```


## Deploy on a Kubernetes cluster

...

## IntelliJ settings

In order to restart application on build in developement mode, configure Intellij:

     File | Settings | Build, Execution, Deployment | Build Tools | Gradle | Runner
     => Delegate IDE build/run actions to gradle.

## TODO

- Sonarqube analysis ?
- Gracefull shutdown
- Deployment
- Health check route
- Document download
- Frontend improvements