# -*- coding: utf-8 -*-
from Config import Config
from commons.Logger import Logger
from services.BuildService import BuildService
from services.DeploymentService import DeploymentService
from services.DevService import DevController


class MainController:

    def __init__(self):
        self.buildService = BuildService()
        self.devService = DevController()
        self.deploymentService = DeploymentService()

    def showHelp(self) -> None:
        Logger.info("""
        
Helper script. Examples:

        $ cli help
        $ cli start
        $ cli build
        $ cli deploy environment-name
        $ cli destroy environment-name
        
        """)

    def processArgs(self, args):
        cleanArgs = self.cleanArgs(args)

        if len(cleanArgs) < 2:
            self.showHelp()
            raise Exception('You must specify a command')

        elif cleanArgs[1] == 'help':
            Logger.info('Help !\n')
            self.showHelp()

        elif cleanArgs[1] == 'start':
            Logger.info('Starting...\n')
            self.devService.dockerComposeStart()

        elif cleanArgs[1] == 'build':
            Logger.info('Building all applications ...\n')
            self.buildService.buildAll(Config.DOCKER_TAG)

        elif cleanArgs[1] == 'deploy':
            Logger.info('Deploying ...\n')
            if len(cleanArgs) < 3:
                raise Exception("You must specify environment name")

            self.deploymentService.deploy(cleanArgs[2], Config.DOCKER_TAG)

        elif cleanArgs[1] == 'destroy':
            Logger.info('Destroying ...\n')

            if len(cleanArgs) < 3:
                raise Exception("Destroy need one argument: environment name")

            self.deploymentService.destroy(cleanArgs[2])

        elif cleanArgs[1] == 'redeploy':
            Logger.info('Re-deploying ...\n')
            if len(cleanArgs) < 3:
                raise Exception("Re-Deploy need one argument: environment name")

            self.deploymentService.destroy(cleanArgs[2])
            self.deploymentService.deploy(cleanArgs[2], Config.DOCKER_TAG)

        else:
            raise Exception("Invalid command: " + " ".join(cleanArgs))

    def cleanArgs(self, arguments):
        return list(map(lambda arg: arg.strip(), arguments))
