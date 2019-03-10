# -*- coding: utf-8 -*-
import os

from AppBuilder import AppBuilder
from Command import Command
from Containers import BaseDockerImage
from Containers import Containers
from Paths import Paths
from Utils import Utils


class DeployHandlers:

    def __init__(self):
        self.appBuilder = AppBuilder()

    def helmDeploy(self, namespace, releaseName):
        self.buildBaseImageIfNecessary()
        self.appBuilder.buildAllApplications()

        self.appBuilder.buildDockerImages(Containers.appContainers)
        self.appBuilder.pushDockerImages(Containers.appContainers)

        self.createNamespace(namespace)
        self.createRegistrySecret(namespace)
        self.deployHelmChart(namespace, releaseName)

    def createNamespace(self, namespace):
        try:
            Command.runSync("kubectl create namespace " + namespace)
        except:
            Utils.log('Error while creating namespace')

    def createRegistrySecret(self, namespace):
        createSecret = "kubectl create secret generic registry-secret"
        createSecret += " --from-file=.dockerconfigjson=" + os.path.expanduser('~/.docker/config.json')
        createSecret += " --type=kubernetes.io/dockerconfigjson "
        createSecret += " --namespace " + namespace
        try:
            Command.runSync(createSecret)
        except:
            Utils.log('Error while creating registry secret')


    def helmDestroy(self, releaseName):
        self.destroyHelmChart(releaseName)

    def deployHelmChart(self, namespace, releaseName):
        helmUpgrade = "helm upgrade "
        helmUpgrade += " --install --force " + releaseName
        helmUpgrade += " --wait --timeout 500"
        helmUpgrade += " --namespace " + namespace
        helmUpgrade += " " + Paths.HELM_CHART_PATH
        Command.runSync(helmUpgrade)

    def destroyHelmChart(self, releaseName):
        Command.runSync("helm delete --purge " + releaseName)

    def buildBaseImageIfNecessary(self):
        out = Command.runSyncAndGetOutput("docker images -q " + BaseDockerImage.name)
        if len(out) < 1:
            self.appBuilder.buildBaseImage()
