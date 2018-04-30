#!/usr/bin/env python3

import subprocess
import sys
from scripts import ActionHandlers
from scripts import Utils

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

