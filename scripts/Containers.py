# -*- coding: utf-8 -*-


# TODO: move this class to a setup file, on top level directory
# TODO: rename to Application


class Container:
    def __init__(self, serviceName=None, devEnvFile=None, dockerBuildDir=None, isServiceContainer=False):

        # Docker compose service name
        self.serviceName = serviceName

        # If true, container cannot be build with gradle
        self.isServiceContainer = isServiceContainer

        # Environment file loaded before dev launch
        self.devEnvFile = devEnvFile

        # Docker build directory
        self.dockerBuildDir = dockerBuildDir


class Containers:

    allContainers = [
        Container(serviceName="gateway", 
                  devEnvFile="./applications/gateway/setenv-dev.sh",
                  dockerBuildDir="./applications/gateway"),

        Container(serviceName="signature-service",
                  devEnvFile="./applications/signature-service/setenv-dev.sh",
                  dockerBuildDir="./applications/signature-service"),

        Container("mongodb", isServiceContainer=True),
        Container("nats", isServiceContainer=True)
    ]

    serviceContainers = list(filter(lambda ctr: ctr.isServiceContainer, allContainers))

    @staticmethod
    def getContainer(name):
        return list(filter(lambda ctr: ctr.serviceName == name, Containers.allContainers))[0]

    @staticmethod
    def getServiceContainers():
        return Containers.serviceContainers

    @staticmethod
    def getContainersByName(containerNames):
        result = list(filter(lambda ctr: ctr.serviceName in containerNames, Containers.allContainers))
        if len(result) != len(containerNames):
            containersNotFound = list(filter(lambda name: name not in Containers.allContainers, containerNames))
            raise Exception("Some containers were not found: " + " ".join(containersNotFound))
        return result