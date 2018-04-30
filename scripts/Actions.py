# -*- coding: utf-8 -*-
from .utils import Paths
from .utils import Utils


class ActionHandlers:

    def buildAll(self):
        self.buildFrontend()
        self.buildAllApplications()

    def buildFrontend(self):
        Utils.runCommand("npm install && npm run update-gateway", Paths.FRONTEND_ROOT)

    def buildAllApplications(self):
        Utils.runCommand("./gradlew build -x test")

    def buildApplications(self, containers):
        self.assertNoServiceContainers(containers)

        appStr = self.joinGradleAppNames(containers, "build")

        print("./gradlew " + appStr + " -x test")
        Utils.runCommand("./gradlew " + appStr + " -x test")

    def startDockerCompose(self, containers):
        containersStr = self.joinContainerNames(containers)
        Utils.runCommand("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def stopDockerCompose(self, containers):
        containersStr = self.joinContainerNames(containers)
        print(containersStr)
        if len(containersStr) > 0:
            Utils.runCommand("docker-compose stop " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        else:
            Utils.runCommand("docker-compose down", Paths.DOCKER_COMPOSE_ROOT)

    def restartDockerContainers(self, containers):
        containersStr = self.joinContainerNames(containers)
        Utils.runCommand("docker-compose restart " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

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
        Utils.runCommand("source " + containers[0].devEnvFile + " && ./gradlew " + appStr)

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
