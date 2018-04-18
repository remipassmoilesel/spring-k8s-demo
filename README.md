# Spring on Kubernetes !

## Purpose

This is a small application built with Spring Boot and VueJS, intended to be deployed on a Kubernetes cluster. 
With this application, you can upload documents, sign them then check them with a PGP key. 

A living demo on a Kubernetes cluster is available here: [spring-demo.remi-pace.fr](https://spring-demo.remi-pace.fr) - 
sorry for the self-signed certificate, I have done too many experiments with Let's Encrypt :)

## Why Kubernetes ?

Kubernetes offer many advantages:

- With Kubernetes and Docker, deploying an application is really easy
- You can deploy any type of technology, including technologies that are normally incompatible with each other
- Applications are monitored and responsive: they heal themselves, and they can scale automatically according to their activities
- The use of servers is optimized: no RAM or CPU that sleep and cost money

## Use and test application

⚠️ Warning: This application has only been tested on Ubuntu and Alpine Linux

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
    $ ./scripts/package-and-launch.sh
```

To launch tests, you will need to enable the database:

```
    $ ./scripts/launch-tests.sh
```

## Deploy on a Kubernetes cluster

You will need a running Kubernetes cluster and an ingress controller. You can make trials quickly and easily with 
[Minikube](https://kubernetes.io/docs/tutorials/stateless-application/hello-minikube/).

Then adapt the deployment file with the address of your MariaDB database: [kubernetes/app-deployment.yaml](kubernetes/app-deployment.yaml)

And finally deploy:

```
    $ ./scripts/k8s-deploy.sh
```

## IntelliJ settings for development

In order to restart application on build in developement mode, configure Intellij:

     File | Settings | Build, Execution, Deployment | Build Tools | Gradle | Runner
     => Delegate IDE build/run actions to gradle.

## TODO

- Application auto-scaling setup
- Gracefull shutdown, in order to close properly databases connection on stop
- A better health check route, in order to check more application components
- Sonarqube analysis ?
