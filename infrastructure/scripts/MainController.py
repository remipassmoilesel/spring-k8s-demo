# -*- coding: utf-8 -*-
import argparse

from Config import Config
from services.BuildService import BuildService
from services.DashboardService import DashboardService
from services.DeploymentService import DeploymentService
from services.DevService import DevController


class MainController:

    def __init__(self):
        self.buildService = BuildService()
        self.devService = DevController()
        self.deploymentService = DeploymentService()
        self.dashboardService = DashboardService()

    def __getDescription(self) -> str:
        return """
    $$\      $$\ $$\                                                                         $$$$$$\        $$$$\  
    $$ | $\  $$ |$$ |                                                                        \_$$  _|      $$  $$\ 
    $$ |$$$\ $$ |$$$$$$$\   $$$$$$\   $$$$$$\   $$$$$$\         $$$$$$\  $$$$$$\$$$$\          $$ |        \__/$$ |
    $$ $$ $$\$$ |$$  __$$\ $$  __$$\ $$  __$$\ $$  __$$\        \____$$\ $$  _$$  _$$\         $$ |           $$  |
    $$$$  _$$$$ |$$ |  $$ |$$$$$$$$ |$$ |  \__|$$$$$$$$ |       $$$$$$$ |$$ / $$ / $$ |        $$ |          $$  / 
    $$$  / \$$$ |$$ |  $$ |$$   ____|$$ |      $$   ____|      $$  __$$ |$$ | $$ | $$ |        $$ |          \__/  
    $$  /   \$$ |$$ |  $$ |\$$$$$$$\ $$ |      \$$$$$$$\       \$$$$$$$ |$$ | $$ | $$ |      $$$$$$\         $$\   
    \__/     \__|\__|  \__| \_______|\__|       \_______|       \_______|\__| \__| \__|      \______|        \__|  
                                                                                                                   
                                                                                                                   
                                                                                                                       
Helper script. Examples:

        $ cli --help
        $ cli --start
        $ cli --build
        $ cli --kubernetes-dashboard
        $ cli --build --deploy -e environment-name
        $ cli --destroy -e environment-name
        
        """

    def processArgs(self) -> None:
        parser = argparse.ArgumentParser(description=self.__getDescription(),
                                         formatter_class=argparse.RawTextHelpFormatter)
        parser.add_argument('--start', action='store_true', help='Start all applications')
        parser.add_argument('--build', action='store_true', help='Build all applications and docker images')
        parser.add_argument('--kubernetes-dashboard', action='store_true', help='Show Kubernetes dashboard')
        parser.add_argument('--deploy', action='store_true', help='Deploy the specified environment')
        parser.add_argument('--destroy', action='store_true', help='Destroy the specified environment')

        parser.add_argument('--environment', '-e', help='Environment name')

        knownArgs = parser.parse_args()

        if knownArgs.start:
            self.buildService.buildAll(Config.DOCKER_TAG)
            self.devService.startAll()

        elif knownArgs.build:
            self.buildService.buildAll(Config.DOCKER_TAG)

        elif knownArgs.deploy:
            environment = knownArgs.environment
            if not environment:
                raise Exception("You must specify environment name")

            if knownArgs.build:
                self.buildService.buildAll(Config.DOCKER_TAG)
                self.buildService.pushDockerImages(Config.DOCKER_TAG)

            self.deploymentService.deploy(environment, Config.DOCKER_TAG)

        elif knownArgs.destroy:
            environment = knownArgs.environment
            if not environment:
                raise Exception("You must specify environment name")

            self.deploymentService.destroy(environment)

        elif knownArgs.kubernetes_dashboard:
            self.dashboardService.showDashboard()

        else:
            raise Exception("Invalid command")

    def cleanArgs(self, arguments):
        return list(map(lambda arg: arg.strip(), arguments))
