#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import signal
import sys
import traceback

from scripts import ActionHandlers
from scripts import Containers
from scripts import Utils, TermStyle

DEBUG=False


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
        Utils.log('\t$ dev build-start gateway')
        Utils.log('\t$ dev local signature-service')
        Utils.log('\t$ dev test signature-service')
        Utils.log('\t$ dev deploy dev application1')
        Utils.log('\t$ dev destroy dev application1')

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

            self.actions.demo()

        elif cleanArgs[1] == 'start':
            Utils.log('Start containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)

            self.actions.dockerComposeStart(containers)

        elif cleanArgs[1] == 'build-start':
            Utils.log('Build and start containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)

            self.actions.dockerComposeBuildAndStart(containers)

        elif cleanArgs[1] == 'stop':
            Utils.log('Stop containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)

            self.actions.dockerComposeStop(containers)

        elif cleanArgs[1] == 'restart':
            Utils.log('Build and restart containers...\n')
            containers = self.getContainersFromArgs(cleanArgs)

            self.actions.dockerComposebuildAndRestart(containers)

        elif cleanArgs[1] == 'local':
            Utils.log('Local launch...\n')
            containers = self.getContainersFromArgs(cleanArgs)

            self.actions.launchLocal(containers)

        elif cleanArgs[1] == 'test':
            Utils.log('Launch test ...\n')
            containers = self.getContainersFromArgs(cleanArgs)

            self.actions.testApplications(containers)

        elif cleanArgs[1] == 'deploy':
            Utils.log('Deploying helm chart ...\n')
            if len(cleanArgs) < 4:
                raise Exception("Deploy need two arguments: environment name and release name")

            self.actions.helmDeploy(cleanArgs[2], cleanArgs[3])

        elif cleanArgs[1] == 'destroy':
            Utils.log('Destroying helm chart ...\n')

            if len(cleanArgs) < 3:
                raise Exception("Destroy need one argument: release name")

            self.actions.helmDestroy(cleanArgs[2])

        elif cleanArgs[1] == 'redeploy':
            Utils.log('Re-deploying helm chart ...\n')
            if len(cleanArgs) < 4:
                raise Exception("Re-Deploy need two arguments: environment name and release name")

            self.actions.helmRedeploy(cleanArgs[2], cleanArgs[3])

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

    Utils.log('\n 💪 Dev helper 💨 💨 💨 \n')
    mainApp = MainApplication()

    try:
        mainApp.handleInterrupt()
        mainApp.processArgs(sys.argv)
        mainApp.waitUntilAllAppDone()
    except Exception as err:
        Utils.log("Error: {0}".format(err), termStyle=TermStyle.RED)
        if DEBUG:
            traceback.print_exc()

