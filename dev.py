#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import traceback
import signal
from scripts import ActionHandlers, TermStyle
from scripts import Utils
from scripts import Containers

DEBUG=True


class MainApplication:
    def __init__(self):
        self.actions = ActionHandlers()

    def showHelp(self):
        Utils.log('Helper script to control applications for development purposes')
        Utils.log('')
        Utils.log('Examples: ')
        Utils.log('\t$ dev help')
        Utils.log('\t$ dev demo')
        Utils.log('\t$ dev start')
        Utils.log('\t$ dev start nats mongodb')
        Utils.log('\t$ dev stop')
        Utils.log('\t$ dev stop nats mongodb')
        Utils.log('\t$ dev restart gateway')
        Utils.log('\t$ dev local signature-service')
        Utils.log('\t$ dev test signature-service')

    def processArgs(self, args):
        cleanArgs = self.cleanArgs(args)

        # print(cleanArgs)

        if len(cleanArgs) < 2:
            self.showHelp()
            raise Exception('You must specify a command')

        elif cleanArgs[1] == 'help':
            Utils.log('Help !\n')
            self.showHelp()

        elif cleanArgs[1] == 'demo':
            Utils.log('Launching demo ...\n')
            self.actions.buildAll()
            self.actions.startDockerCompose([])

        elif cleanArgs[1] == 'start':
            Utils.log('Start containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.startDockerCompose(containers)

        elif cleanArgs[1] == 'stop':
            Utils.log('Stop containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.stopDockerCompose(containers)

        elif cleanArgs[1] == 'restart':
            Utils.log('Build and restart containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.buildAndRestart(containers)

        elif cleanArgs[1] == 'local':
            Utils.log('Local launch...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.launchLocal(containers)

        elif cleanArgs[1] == 'test':
            Utils.log('Launch test ...\n')
            containers = self.getContainersFromArgs(cleanArgs)
            self.actions.testApplications(containers)

        else:
            raise Exception("Invalid command: " + " ".join(cleanArgs))

    def cleanArgs(self, arguments):
        return list(map(lambda arg: arg.strip(), arguments))

    def getContainersFromArgs(self, cleanArgs):
        if len(cleanArgs) < 3:
            return []
        elif cleanArgs[2] == 'svc':
            return Containers.getServiceContainers()
        else:
            return Containers.getContainersByName(cleanArgs[2:])

    def handleInterrupt(self):
        def signal_handler(signal, frame):
            Utils.log("\nApplication will stop soon ...\n")

            self.actions.killAllAndWait()
            sys.exit(1)

        signal.signal(signal.SIGINT, signal_handler)

    def waitUntilAllAppDone(self):
        self.actions.waitUntilAllAppFinished()


if __name__ == '__main__':

    Utils.log('\nDev helper 💪💪💪\n')
    mainApp = MainApplication()

    try:
        mainApp.handleInterrupt()
        mainApp.processArgs(sys.argv)
        mainApp.waitUntilAllAppDone()
    except Exception as err:
        Utils.log("Error: {0}".format(err), termStyle=TermStyle.RED)
        if DEBUG:
            traceback.print_exc()

