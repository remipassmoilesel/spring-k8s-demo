# -*- coding: utf-8 -*-
import fcntl
import os
from .Paths import Paths
from subprocess import Popen, PIPE

ENCODING = 'utf-8'

class TermStyle:
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
    def log(message='', data=None, termStyle=TermStyle.GREEN):
        print(Utils.colorize(message, termStyle=termStyle))
        if data:
            print(data)

    @staticmethod
    def colorize(message, termStyle=TermStyle.GREEN):
        return termStyle + str(message) + TermStyle.RESET

    @staticmethod
    def nonBlockReadStream(stream):
        fd = stream.fileno()
        fl = fcntl.fcntl(fd, fcntl.F_GETFL)
        fcntl.fcntl(fd, fcntl.F_SETFL, fl | os.O_NONBLOCK)
        try:
            return stream.read()
        except:
            return None

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
                for line in out.decode(ENCODING).rstrip().splitlines():
                    print(line)

        stderr = self.process.stderr
        if stderr is not None:
            out = Utils.nonBlockReadStream(stderr)
            if out is not None:
                for line in out.decode(ENCODING).rstrip().splitlines():
                    print(line)
