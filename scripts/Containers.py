# -*- coding: utf-8 -*-


# TODO: move this class to a setup file, on top level directory
# TODO: rename to Application


class Container:
    def __init__(self,
                 serviceName=None,
                 devEnvFile=None,
                 dockerBuildDir=None,
                 imageName=None,
                 isServiceContainer=False):

        # Docker compose service name
        self.serviceName = serviceName

        # If true, container cannot be build with gradle
        self.isServiceContainer = isServiceContainer

        # Environment file loaded before dev launch
        self.devEnvFile = devEnvFile

        # Docker build directory and image name
        self.dockerBuildDir = dockerBuildDir
        self.imageName = imageName


class Containers:

    allContainers = [
        Container(serviceName="gateway", 
                  devEnvFile="./applications/gateway/setenv-dev.sh",
                  dockerBuildDir="./applications/gateway",
                  imageName="docker.remi-pace.fr/k8sdemo.gateway:0.1"),

        Container(serviceName="signature-service",
                  devEnvFile="./applications/signature-service/setenv-dev.sh",
                  dockerBuildDir="./applications/signature-service",
                  imageName="docker.remi-pace.fr/k8sdemo.signature-service:0.1"),

        Container("mongodb", isServiceContainer=True),
        Container("nats", isServiceContainer=True)
    ]

    appContainers = list(filter(lambda ctr: not ctr.isServiceContainer, allContainers))
    serviceContainers = list(filter(lambda ctr: ctr.isServiceContainer, allContainers))

    @staticmethod
    def getContainer(name):
        return list(filter(lambda ctr: ctr.serviceName == name, Containers.allContainers))[0]

    @staticmethod
    def getServiceContainers():
        return Containers.serviceContainers

    @staticmethod
    def getAppContainers():
        return Containers.appContainers

    @staticmethod
    def getContainersByName(containerNames):
        result = list(filter(lambda ctr: ctr.serviceName in containerNames, Containers.allContainers))
        if len(result) != len(containerNames):
            containersNotFound = list(filter(lambda name: name not in Containers.allContainers, containerNames))
            raise Exception("Some containers were not found: " + " ".join(containersNotFound))
        return result

assert(len(Containers.appContainers) > 0)
assert(len(Containers.serviceContainers) > 0)