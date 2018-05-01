# -*- coding: utf-8 -*-
import time
from .Paths import Paths
from .Utils import Utils
from .Command import Command
from .Containers import Containers


class ActionHandlers:

    def __init__(self):
        self.commands = []

    def demo(self):
        self.buildFrontend()
        self.buildAllApplications()
        self.dockerComposeStart([])

    def helmDeploy(self, namespace, releaseName):
        self.buildAllApplications()
        map(lambda ctr: self.buildDockerImage(ctr), Containers.appContainers)
        map(lambda ctr: self.pushDockerImage(ctr), Containers.appContainers)
        self.deployHelmChart(namespace, releaseName)

    def helmDestroy(self, releaseName):
        self.destroyHelmChart(releaseName)

    def dockerComposeBuildAndStart(self, containers):
        self.buildAllApplications()
        self.dockerComposeStart(containers)

    def dockerComposebuildAndRestart(self, containers):
        if len(containers) > 0:
            self.buildApplications(containers)
        else:
            self.buildAllApplications()
        self.dockerComposeRestart(containers)

    def buildAll(self):
        self.buildFrontend()
        self.buildAllApplications()

    def buildFrontend(self):
        Command.runSync("npm install && npm run update-gateway", Paths.FRONTEND_ROOT)

    def buildAllApplications(self):
        Command.runSync("./gradlew build -x test")

    def buildApplications(self, containers):
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "build")
        Command.runSync("./gradlew " + appStr + " -x test")

    def dockerComposeStart(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        comm = Command.runAsync("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        self.commands.append(comm)

    def dockerComposeStop(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        if len(containersStr) > 0:
            comm = Command.runAsync("docker-compose stop " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

        else:
            comm = Command.runAsync("docker-compose down", Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

    def dockerComposeRestart(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        Command.runSync("docker-compose up -d --force-recreate " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def launchLocal(self, containers):
        Utils.assertAtLeastOneContainer(containers, 1)
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "bootRun")
        comm = Command.runAsync("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)
        self.commands.append(comm)

    def testApplications(self, containers):
        Utils.assertAtLeastOneContainer(containers)
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "test")
        comm = Command.runAsync("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)
        self.commands.append(comm)

    def buildDockerImage(self, ctr):
        Command.runSync("docker build . -t " + ctr.imageName, cwd=ctr.dockerBuildDir)

    def pushDockerImage(self, ctr):
        Command.runSync("docker push " + ctr.imageName)

    def deployHelmChart(self, namespace, releaseName):
        Command.runSync("helm install " + Paths.HELM_CHART_PATH + " -n " + releaseName + " --namespace " + namespace)

    def destroyHelmChart(self, releaseName):
        Command.runSync("helm delete --purge " + releaseName)

    def exit(self, code=0):
        exit(code)

    def killAllAndWait(self):
        for comm in self.commands:
            comm.kill()
        self.waitUntilAllAppFinished()

    def isThereAliveCommands(self):
        for comm in self.commands:
            if comm.isAlive():
                return True
        return False

    def waitUntilAllAppFinished(self):
        while self.isThereAliveCommands():
            self.printCommandsOutput()
            time.sleep(0.1)

    def printCommandsOutput(self):
        for com in self.commands:
            com.printOutput()


