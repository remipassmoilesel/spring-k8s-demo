# -*- coding: utf-8 -*-
import time
from .Paths import Paths
from .Utils import Utils
from .Command import Command
from .Containers import Containers
from .AppBuilder import AppBuilder
from .DevHandlers import DevHandlers
from .DeployHandlers import DeployHandlers


class ActionHandlers:

    def __init__(self):
        self.commands = []
        self.appBuilder = AppBuilder()
        self.devHandlers = DevHandlers()
        self.deployHandlers = DeployHandlers()

    def demo(self):
        asyncCommand = self.devHandlers.demo()
        self.commands.append(asyncCommand)

    def dockerComposeBuildAndStart(self, containers):
        asyncCommand = self.devHandlers.dockerComposeBuildAndStart(containers)
        self.commands.append(asyncCommand)

    def dockerComposebuildAndRestart(self, containers):
        self.devHandlers.dockerComposebuildAndRestart(containers)

    def dockerComposeStart(self, containers):
        asyncCommand = self.devHandlers.dockerComposeStart(containers)
        self.commands.append(asyncCommand)

    def dockerComposeStop(self, containers):
        asyncCommand = self.devHandlers.dockerComposeStop(containers)
        self.commands.append(asyncCommand)

    def launchLocal(self, containers):
        asyncCommand = self.devHandlers.launchLocal(containers)
        self.commands.append(asyncCommand)

    def testApplications(self, containers):
        asyncCommand = self.devHandlers.testApplications(containers)
        self.commands.append(asyncCommand)

    def helmDeploy(self, namespace, releaseName):
        self.deployHandlers.helmDeploy(namespace, releaseName)

    def helmDestroy(self, releaseName):
        self.deployHandlers.helmDestroy(releaseName)

    def helmRedeploy(self, namespace, releaseName):
        self.deployHandlers.helmDestroy(releaseName)
        self.deployHandlers.helmDeploy(namespace, releaseName)

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



