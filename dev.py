#!/usr/bin/env python3

from scripts import ActionHandlers
from scripts import Utils
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
            services = self.getServicesToLaunch(cleanArgs)
            self.actions.startDockerCompose(services)
            self.actions.exit()

        if cleanArgs[1] == 'stop':
            Utils.log('Stop docker compose...\n')
            services = self.getServicesToLaunch(cleanArgs)
            self.actions.stopDockerCompose(services)
            self.actions.exit()

    def getServicesToLaunch(self, cleanArgs):
        if len(cleanArgs) < 3:
            return []
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

