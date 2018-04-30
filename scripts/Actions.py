# -*- coding: utf-8 -*-
from .utils import Utils
from .utils import Paths


class ActionHandlers:

    # TODO: use absolute paths from path.py
    def buildAll(self):
        self.buildFrontend()
        self.buildApplications()

    def buildFrontend(self):
        Utils.runCommand("npm install && npm run update-gateway", Paths.FRONTEND_ROOT)

    def buildApplications(self):
        Utils.runCommand("./gradlew build -x test")

    def startDockerCompose(self, containers = []):
        containersStr = " ".join(containers)
        Utils.runCommand("docker-compose up " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def stopDockerCompose(self, containers = []):
        containersStr = " ".join(containers)
        Utils.runCommand("docker-compose down " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def restartContainers(self, containers = []):
        containersStr = " ".join(containers)
        Utils.runCommand("docker-compose down " + containersStr, Paths.DOCKER_COMPOSE_ROOT)

    def showHelp(self):
        Utils.log('Control application in development environment')
        Utils.log('')
        Utils.log('Examples: ')
        Utils.log('\t$ dev start')
        Utils.log('\t$ dev stop')

    def exit(self, code=0):
        exit(code)
