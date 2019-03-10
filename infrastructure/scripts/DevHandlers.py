# -*- coding: utf-8 -*-
from Paths import Paths
from Utils import Utils
from Command import Command
from AppBuilder import AppBuilder


class DevHandlers:

    def __init__(self):
        self.appBuilder = AppBuilder()

    def demo(self):
        self.appBuilder.buildFrontend()
        self.appBuilder.buildAllApplications()
        return self.dockerComposeStart([])

    def launchLocal(self, containers):
        Utils.assertAtLeastOneContainer(containers, 1)
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "bootRun")
        return Command.runAsync("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)

    def testApplications(self, containers):
        Utils.assertAtLeastOneContainer(containers)
        Utils.assertNoServiceContainers(containers)

        appStr = Utils.joinGradleAppNames(containers, "test")
        return Command.runAsync("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)

    def dockerComposeBuildAndStart(self, containers):
        self.appBuilder.buildAllApplications()
        return self.dockerComposeStart(containers)

    def dockerComposebuildAndRestart(self, containers):
        if len(containers) > 0:
            self.appBuilder.buildApplications(containers)
        else:
            self.appBuilder.buildAllApplications()
        self.dockerComposeRestart(containers)

    def dockerComposeStart(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        return Command.runAsync("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def dockerComposeStop(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        if len(containersStr) > 0:
            return Command.runAsync("docker-compose stop " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

        else:
            return Command.runAsync("docker-compose down", Paths.DOCKER_COMPOSE_ROOT)

    def dockerComposeRestart(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        Command.runSync("docker-compose up -d --force-recreate " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
