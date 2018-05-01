from subprocess import run, Popen, PIPE, check_output
from .Paths import Paths
from .Utils import Utils, TermStyle

CONSOLE_ENCODING = 'utf-8'

class Command:

    @staticmethod
    def runAsync(shellCommand, cwd = Paths.ROOT):
        Utils.log('Executing command: \n' + shellCommand, termStyle=TermStyle.BLUE)
        comm = Command(shellCommand, cwd)
        comm.executeAsync()
        return comm

    @staticmethod
    def runSync(shellCommand, cwd = Paths.ROOT):
        Utils.log('Executing command: \n' + shellCommand, termStyle=TermStyle.BLUE)
        comm = Command(shellCommand, cwd)
        comm.executeSync()

    @staticmethod
    def runSyncAndGetOutput(shellCommand, cwd = Paths.ROOT):
        comm = Command(shellCommand, cwd)
        return comm.executeSyncAndGetOutput()

    def __init__(self, shellCommand, workingDir):
        self.shellCommand = shellCommand
        self.workingDir = workingDir

    def executeSync(self):
        self.process = run(self.shellCommand, shell=True, cwd=self.workingDir, executable="/bin/bash")

    def executeAsync(self):
        self.process = Popen(self.shellCommand, shell=True, cwd=self.workingDir, executable="/bin/bash", stderr=PIPE, stdout=PIPE)

    def executeSyncAndGetOutput(self):
        return check_output(self.shellCommand, shell=True, cwd=self.workingDir, executable="/bin/bash")

    def isAlive(self):
        return self.process is not None and self.process.poll() is None

    def kill(self):
        if self.isAlive():
            self.process.kill()

    def printOutput(self):
        if self.process is None:
            return

        stdout = self.process.stdout
        if stdout is not None:
            out = Utils.nonBlockReadStream(stdout)
            if out is not None:
                for line in out.decode(CONSOLE_ENCODING).rstrip().splitlines():
                    print(line)

        stderr = self.process.stderr
        if stderr is not None:
            out = Utils.nonBlockReadStream(stderr)
            if out is not None:
                for line in out.decode(CONSOLE_ENCODING).rstrip().splitlines():
                    print(line)


