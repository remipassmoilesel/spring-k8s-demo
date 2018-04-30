# -*- coding: utf-8 -*-
from .utils import Utils
from .utils import Paths


class ActionHandlers:

    def buildAll(self):
        self.buildFrontend()
        self.buildAllApplications()

    def buildFrontend(self):
        Utils.runCommand("npm install && npm run update-gateway", Paths.FRONTEND_ROOT)

    def buildAllApplications(self):
        Utils.runCommand("./gradlew build -x test")

    def startDockerCompose(self, containers):
        containersStr = self.joinContainerNames(containers)
        Utils.runCommand("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def stopDockerCompose(self, containers):
        containersStr = self.joinContainerNames(containers)
        print(containersStr)
        if len(containersStr) > 0:
            Utils.runCommand("docker-compose stop " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        else:
            Utils.runCommand("docker-compose down " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def buildAndRestart(self, containers):
        if len(containers) > 0:
            appStr = self.joinGradleAppNames(containers, "build")
            print("./gradlew " + appStr + " -x test")
            Utils.runCommand("./gradlew " + appStr + " -x test")

            containersStr = self.joinContainerNames(containers)
            Utils.runCommand("docker-compose restart " + containersStr, Paths.DOCKER_COMPOSE_ROOT)
        else:
            self.buildAllApplications()
            self.startDockerCompose([])

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
