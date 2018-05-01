# -*- coding: utf-8 -*-
from .AppBuilder import AppBuilder
from .Command import Command
from .Containers import Containers
from .Paths import Paths


class DeployHandlers:

    def __init__(self):
        self.appBuilder = AppBuilder()

    def helmDeploy(self, namespace, releaseName):
        self.appBuilder.buildAllApplications()

        self.appBuilder.buildDockerImages(Containers.appContainers)
        self.appBuilder.pushDockerImages(Containers.appContainers)

        self.deployHelmChart(namespace, releaseName)

    def helmDestroy(self, releaseName):
        self.destroyHelmChart(releaseName)

    def deployHelmChart(self, namespace, releaseName):
        Command.runSync("helm install " + Paths.HELM_CHART_PATH + " -n " + releaseName + " --namespace " + namespace)

    def destroyHelmChart(self, releaseName):
        Command.runSync("helm delete --purge " + releaseName)

