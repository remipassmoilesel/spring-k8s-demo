# -*- coding: utf-8 -*-
from Command import Command
from Paths import Paths
from Utils import Utils
from Containers import BaseDockerImage


class AppBuilder:

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

    def buildDockerImages(self, containers):
        for ctr in containers:
            self.__buildImage(ctr.imageName, ctr.dockerBuildDir)

    def pushDockerImages(self, containers):
        for ctr in containers:
            Command.runSync("docker push " + ctr.imageName)

    def buildBaseImage(self):
        self.__buildImage(BaseDockerImage.name, BaseDockerImage.dockerBuildDir)

    def __buildImage(self, imageName, dockerBuildDir):
        Command.runSync("docker build . -t " + imageName, cwd=dockerBuildDir)
