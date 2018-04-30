#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from scripts import ActionHandlers
from scripts import Utils
from scripts import Containers
import sys

# TODO: handle CTRL+C and stop docker compose properly

class ArgParser:
    def __init__(self):
        self.actions = ActionHandlers()

    def processArgs(self, args):
        cleanArgs = self.cleanArgs(args)

        # print(cleanArgs)

        if len(cleanArgs) < 2:
            self.actions.showHelp()
            self.actions.exit(1)

        if cleanArgs[1] == 'help':
            Utils.log('Help !\n')
            self.actions.showHelp()
            self.actions.exit()

        if cleanArgs[1] == 'demo':
            Utils.log('Launching demo ...\n')
            self.actions.buildAll()
            self.actions.startDockerCompose()
            self.actions.exit()

        if cleanArgs[1] == 'start':
            Utils.log('Start docker compose...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.startDockerCompose(containers)
            self.actions.exit()

        if cleanArgs[1] == 'stop':
            Utils.log('Stop docker compose...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.stopDockerCompose(containers)
            self.actions.exit()

        if cleanArgs[1] == 'restart':
            Utils.log('Restart and build containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.restartContainers(containers)
            self.actions.exit()

    def getContainersFromArgs(self, cleanArgs):
        if len(cleanArgs) < 3:
            return []
        elif cleanArgs[2] == 'service-containers':
            return Containers.getServiceContainerNames()
        else:
            return cleanArgs[2:]

    def cleanArgs(self, arguments):
        res = []
        for arg in arguments:
            res.append(arg.strip())
        return res


if __name__ == '__main__':
    Utils.log('Dev helper 💪💪💪')
    Utils.log()

    argParser = ArgParser()
    argParser.processArgs(sys.argv)

