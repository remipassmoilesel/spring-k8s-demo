import subprocess

from commons.Logger import Colors, Logger


class Shell:

    @staticmethod
    def run(command, cwd=None):
        Logger.printColor("\nExecuting: " + command + " \t\tworkingDirectory=" + str(cwd) + "\n", Colors.DIM)
        return subprocess.run(command, cwd=cwd, check=True, shell=True)
