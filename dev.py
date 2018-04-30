#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
from scripts import ActionHandlers, TermStyle
from scripts import Utils
from scripts import Containers

# TODO: handle CTRL+C and stop docker compose properly

class ArgParser:
    def __init__(self):
        self.actions = ActionHandlers()

    def processArgs(self, args):
        cleanArgs = self.cleanArgs(args)

        # print(cleanArgs)

        if len(cleanArgs) < 2:
            self.actions.showHelp()
            raise Exception('You must specify a command')

        elif cleanArgs[1] == 'help':
            Utils.log('Help !\n')
            self.actions.showHelp()

        elif cleanArgs[1] == 'demo':
            Utils.log('Launching demo ...\n')
            self.actions.buildAll()
            self.actions.startDockerCompose()

        elif cleanArgs[1] == 'start':
            Utils.log('Start docker compose...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.startDockerCompose(containers)

        elif cleanArgs[1] == 'stop':
            Utils.log('Stop docker compose...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.stopDockerCompose(containers)

        elif cleanArgs[1] == 'restart':
            Utils.log('Restart and build containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.restartContainers(containers)

        else:
            raise Exception("Invalid command: " + " ".join(cleanArgs))


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

    try:
        argParser.processArgs(sys.argv)
    except Exception as err:
        Utils.log("Error: {0}".format(err), termStyle=TermStyle.RED)


