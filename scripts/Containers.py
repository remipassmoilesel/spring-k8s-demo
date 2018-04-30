# -*- coding: utf-8 -*-


class Container:
    def __init__(self, serviceName, devEnvFile, isServiceContainer = False):

        # Docker compose service name
        self.serviceName = serviceName

        # If true, container cannot be build with gradle
        self.isServiceContainer = isServiceContainer

        # Environment file loaded before dev launch
        self.devEnvFile = devEnvFile


class Containers:

    allContainers = [
        Container("gateway", "./applications/gateway/setenv-dev.sh"),
        Container("signature-service", "./applications/signature-service/setenv-dev.sh"),

        Container("mongodb", None, isServiceContainer=True),
        Container("nats", None, isServiceContainer=True)
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