#!/usr/bin/env python3

import subprocess
import sys


class TermStyle():
    HEADER = '\033[95m'
    BLUE = '\033[94m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    RED = '\033[91m'
    CYAN = "\033[1;36m"
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'
    RESET = '\033[0m'


class Utils:
    @staticmethod
    def runCommand(command):
        subprocess.run(command, shell=True, check=True)

    @staticmethod
    def log(message='', data=None, termStyle=TermStyle.GREEN):
        print(Utils.colorize(message, termStyle=termStyle))
        if data:
            print(data)

    @staticmethod
    def colorize(message, termStyle=TermStyle.GREEN):
        return termStyle + str(message) + TermStyle.RESET


class ActionHandlers():

    def showHelp(self):
        Utils.log('Control application in development environment')
        Utils.log('')
        Utils.log('Examples: ')
        Utils.log('\t$ dev start')
        Utils.log('\t$ dev stop')

    def exit(self, code=0):
        exit(code)


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
            self.actions.showHelp()
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
