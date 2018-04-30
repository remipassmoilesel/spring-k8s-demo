# -*- coding: utf-8 -*-


class Container:
    def __init__(self, serviceName, buildPath, isServiceContainer = False):

        # Docker compose service name
        self.serviceName = serviceName

        # If specified, gradle build will be applied on restart
        self.buildPath = buildPath

        self.isServiceContainer = isServiceContainer


class Containers:

    ALL = [
        Container("gateway", "./applications/gateway"),
        Container("signature-service", "./applications/signature-service"),

        Container("mongodb", None, True),
        Container("nats", None, True)
    ]

    SERVICE_CONTAINERS = list(filter(lambda ctr: ctr.isServiceContainer, ALL))

    @classmethod
    def getContainer(self, name):
        return list(filter(lambda ctr: ctr.serviceName == name, Containers.ALL))[0]

    @classmethod
    def getServiceContainerNames(cls):
        return map(lambda ctr : ctr.serviceName, Containers.SERVICE_CONTAINERS)
