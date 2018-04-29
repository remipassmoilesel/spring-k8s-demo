
import subprocess
import sys

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
