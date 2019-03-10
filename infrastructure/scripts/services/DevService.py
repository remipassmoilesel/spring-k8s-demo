# -*- coding: utf-8 -*-
from Config import Config
from commons.Shell import Shell
from commons.Utils import Utils
from services.BuildService import BuildService


class DevController:

    def __init__(self):
        self.appBuilder = BuildService()

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

    def dockerComposeStart(self):
        return Shell.run("docker-compose up -d ", Config.DOCKER_COMPOSE_ROOT)

    def dockerComposeStop(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        if len(containersStr) > 0:
            return Command.runAsync("docker-compose stop " + containersStr, Config.DOCKER_COMPOSE_ROOT)

        else:
            return Command.runAsync("docker-compose down", Config.DOCKER_COMPOSE_ROOT)

    def dockerComposeRestart(self, containers):
        containersStr = Utils.joinContainerNames(containers)
        Shell.run("docker-compose up -d --force-recreate " + containersStr, Config.DOCKER_COMPOSE_ROOT)
