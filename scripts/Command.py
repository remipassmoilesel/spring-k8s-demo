from subprocess import Popen, PIPE
from .Paths import Paths
from .Utils import Utils, TermStyle

CONSOLE_ENCODING = 'utf-8'

class Command:

    @staticmethod
    def run(shellCommand, cwd = Paths.ROOT):
        Utils.log('\nExecuting command: ' + shellCommand + "\n", termStyle=TermStyle.BLUE)
        comm = Command(shellCommand, cwd)
        comm.execute()
        return comm

    def __init__(self, shellCommand, workingDir):
        self.shellCommand = shellCommand
        self.workingDir = workingDir

    def execute(self):
        self.process = Popen(self.shellCommand, shell=True, cwd=self.workingDir, stderr=PIPE, stdout=PIPE)

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
