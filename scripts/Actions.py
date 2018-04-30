# -*- coding: utf-8 -*-
import time
from .Paths import Paths
from .Utils import Utils
from .Command import Command


class ActionHandlers:

    def __init__(self):
        self.commands = []

    def buildAndRestart(self, containers):
        if len(containers) > 0:
            self.buildApplications(containers)
            self.restartDockerContainers(containers)
        else:
            self.buildAllApplications()
            self.startDockerCompose([])

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

        print("./gradlew " + appStr + " -x test")
        Command.runSync("./gradlew " + appStr + " -x test")

    def startDockerCompose(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        comm = Command.runAsync("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        self.commands.append(comm)

    def stopDockerCompose(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        print(containersStr)
        if len(containersStr) > 0:
            comm = Command.runAsync("docker-compose stop " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

        else:
            comm = Command.runAsync("docker-compose down", Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

    def restartDockerContainers(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        comm = Command.runAsync("docker-compose restart " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        self.commands.append(comm)

    def launchLocal(self, containers):
        Utils.assertAtLeastOneContainer(containers, 1)
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "bootRun")
        comm = Command.runAsync("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)
        self.commands.append(comm)

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