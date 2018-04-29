from .utils import Utils


class ActionHandlers:

    # TODO: use absolute paths from path.py
    def buildAll(self):
        self.buildFrontend()
        self.buildApplications()

    def buildFrontend(self):
        Utils.runCommand("cd applications/frontend && npm run update-gateway")

    def buildApplications(self):
        Utils.runCommand("./gradlew build -x test")

    def startDockerCompose(self):
        Utils.runCommand("cd docker-compose-dev && docker-compose up")

    def showHelp(self):
        Utils.log('Control application in development environment')
        Utils.log('')
        Utils.log('Examples: ')
        Utils.log('\t$ dev start')
        Utils.log('\t$ dev stop')

    def exit(self, code=0):
        exit(code)
