# -*- coding: utf-8 -*-
import time
from .utils import Paths
from .utils import Utils


class ActionHandlers:

    def __init__(self):
        self.commands = []

    def buildAll(self):
        self.buildFrontend()
        self.buildAllApplications()

    def buildFrontend(self):
        comm = Utils.runCommand("npm install && npm run update-gateway", Paths.FRONTEND_ROOT)
        self.commands.append(comm)

    def buildAllApplications(self):
        comm = Utils.runCommand("./gradlew build -x test")
        self.commands.append(comm)

    def buildApplications(self, containers):
        self.assertNoServiceContainers(containers)

        appStr = self.joinGradleAppNames(containers, "build")

        print("./gradlew " + appStr + " -x test")
        comm = Utils.runCommand("./gradlew " + appStr + " -x test")
        self.commands.append(comm)

    def startDockerCompose(self, containers):
        containersStr = self.joinContainerNames(containers)
        comm = Utils.runCommand("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        self.commands.append(comm)

    def stopDockerCompose(self, containers):
        containersStr = self.joinContainerNames(containers)
        print(containersStr)
        if len(containersStr) > 0:
            comm = Utils.runCommand("docker-compose stop " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

        else:
            comm = Utils.runCommand("docker-compose down", Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

    def restartDockerContainers(self, containers):
        containersStr = self.joinContainerNames(containers)
        comm = Utils.runCommand("docker-compose restart " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        self.commands.append(comm)

    def buildAndRestart(self, containers):
        if len(containers) > 0:
            self.buildApplications(containers)
            self.restartDockerContainers(containers)
        else:
            self.buildAllApplications()
            self.startDockerCompose([])

    def launchDev(self, containers):
        self.assertAtLeastOneContainer(containers, 1)
        self.assertNoServiceContainers(containers)

        appStr = self.joinGradleAppNames(containers, "bootRun")
        comm = Utils.runCommand("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)
        self.commands.append(comm)

    def showHelp(self):
        Utils.log('Control application in development environment')
        Utils.log('')
        Utils.log('Examples: ')
        Utils.log('\t$ dev start')
        Utils.log('\t$ dev stop')

    def exit(self, code=0):
        exit(code)

    def joinGradleAppNames(self, containers, gradleTask):
        tasks = list(map(lambda ctr: ctr.serviceName + ":" + gradleTask, containers))
        return " ".join(tasks)

    def joinContainerNames(self, containers):
        containerNames = list(map(lambda ctr: ctr.serviceName, containers))
        return " ".join(containerNames)

    def assertAtLeastOneContainer(self, containers, number=None):
        if len(containers) < 0:
            raise Exception('You must specify at least one application')

        if number is not None and len(containers) != number:
            raise Exception('Expected ' + number + ' applications exactly')

    def assertNoServiceContainers(self, containers):
        for ctr in containers:
            if ctr.isServiceContainer:
                raise Exception('Services containers are not allowed for this operation')

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
            time.sleep(0.5)

    def printCommandsOutput(self):
        for com in self.commands:
            com.printOutput()