# -*- coding: utf-8 -*-


class Container:
    def __init__(self, serviceName, buildPath, isServiceContainer = False):

        # Docker compose service name
        self.serviceName = serviceName

        # If specified, gradle build will be applied on restart
        self.buildPath = buildPath

        self.isServiceContainer = isServiceContainer


class Containers:

    allContainers = [
        Container("gateway", "./applications/gateway"),
        Container("signature-service", "./applications/signature-service"),

        Container("mongodb", None, True),
        Container("nats", None, True)
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
            raise Exception("Some containers were not found. Found: " + " ".join(containersNotFound))
        return result