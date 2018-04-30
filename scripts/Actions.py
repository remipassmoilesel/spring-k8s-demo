# -*- coding: utf-8 -*-
import time
from .Paths import Paths
from .Utils import Utils
from .Command import Command


class ActionHandlers:

    def __init__(self):
        self.commands = []

    def buildAll(self):
        self.buildFrontend()
        self.buildAllApplications()

    def buildFrontend(self):
        comm = Command.run("npm install && npm run update-gateway", Paths.FRONTEND_ROOT)
        self.commands.append(comm)

    def buildAllApplications(self):
        comm = Command.run("./gradlew build -x test")
        self.commands.append(comm)

    def buildApplications(self, containers):
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "build")

        print("./gradlew " + appStr + " -x test")
        comm = Command.run("./gradlew " + appStr + " -x test")
        self.commands.append(comm)

    def startDockerCompose(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        comm = Command.run("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        self.commands.append(comm)

    def stopDockerCompose(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        print(containersStr)
        if len(containersStr) > 0:
            comm = Command.run("docker-compose stop " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

        else:
            comm = Command.run("docker-compose down", Paths.DOCKER_COMPOSE_ROOT)
            self.commands.append(comm)

    def restartDockerContainers(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        comm = Command.run("docker-compose restart " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        self.commands.append(comm)

    def buildAndRestart(self, containers):
        if len(containers) > 0:
            self.buildApplications(containers)
            self.restartDockerContainers(containers)
        else:
            self.buildAllApplications()
            self.startDockerCompose([])

    def launchDev(self, containers):
        Utils.assertAtLeastOneContainer(containers, 1)
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "bootRun")
        comm = Command.run("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)
        self.commands.append(comm)

    def showHelp(self):
        Utils.log('Control application in development environment')
        Utils.log('')
        Utils.log('Examples: ')
        Utils.log('\t$ dev start')
        Utils.log('\t$ dev stop')

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