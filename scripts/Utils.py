# -*- coding: utf-8 -*-
import fcntl
import os


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

    @staticmethod
    def joinGradleAppNames(containers, gradleTask):
        tasks = list(map(lambda ctr: ctr.serviceName + ":" + gradleTask, containers))
        return " ".join(tasks)

    @staticmethod
    def joinContainerNames(containers):
        containerNames = list(map(lambda ctr: ctr.serviceName, containers))
        return " ".join(containerNames)

    @staticmethod
    def assertAtLeastOneContainer(containers, number=None):
        if len(containers) < 1:
            raise Exception('You must specify at least one application')

        if number is not None and len(containers) != number:
            raise Exception('Expected ' + number + ' applications exactly')

    @staticmethod
    def assertNoServiceContainers(containers):
        for ctr in containers:
            if ctr.isServiceContainer:
                raise Exception('Services containers are not allowed for this operation')

