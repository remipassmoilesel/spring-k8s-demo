# -*- coding: utf-8 -*-
from Config import Config
from commons.DockerImage import DockerImage
from commons.Logger import Logger
from commons.Shell import Shell


class BuildService:

    def __showBanner(self):
        Logger.info("""
        
    ██╗████████╗    ██╗    ██╗██╗██╗     ██╗         ██████╗ ███████╗     ██████╗ ██╗  ██╗             
    ██║╚══██╔══╝    ██║    ██║██║██║     ██║         ██╔══██╗██╔════╝    ██╔═══██╗██║ ██╔╝             
    ██║   ██║       ██║ █╗ ██║██║██║     ██║         ██████╔╝█████╗      ██║   ██║█████╔╝              
    ██║   ██║       ██║███╗██║██║██║     ██║         ██╔══██╗██╔══╝      ██║   ██║██╔═██╗              
    ██║   ██║       ╚███╔███╔╝██║███████╗███████╗    ██████╔╝███████╗    ╚██████╔╝██║  ██╗    ██╗██╗██╗
    ╚═╝   ╚═╝        ╚══╝╚══╝ ╚═╝╚══════╝╚══════╝    ╚═════╝ ╚══════╝     ╚═════╝ ╚═╝  ╚═╝    ╚═╝╚═╝╚═╝
        
        """)

    def buildAll(self, dockerTag: str) -> None:
        self.__showBanner()
        self.buildFrontend()
        self.buildAllApplications()
        self.buildDockerImages(dockerTag)

    def buildFrontend(self) -> None:
        Shell.run("npm install && npm run update-gateway", Config.FRONTEND_ROOT)

    def buildAllApplications(self) -> None:
        Shell.run("./gradlew build -x test", cwd=Config.PROJECT_ROOT)

    def buildDockerImages(self, tag: str) -> None:
        self.__buildImage(Config.getBaseDockerImage(), tag)
        for image in Config.getDockerImages():
            self.__buildImage(image, tag)

    def pushDockerImages(self, tag: str) -> None:
        for image in Config.getDockerImages():
            self.__pushImage(image, tag)

    def __buildImage(self, image: DockerImage, tag: str) -> None:
        Shell.run("docker build . -t " + image.getFullName(tag), cwd=image.location)

    def __pushImage(self, image: DockerImage, tag: str) -> None:
        Shell.run("docker push " + image.getFullName(tag), cwd=Config.PROJECT_ROOT)
