#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import traceback

from MainController import MainController
from commons.Logger import Logger

if __name__ == '__main__':

    Logger.success('\n 💪 Dev helper 💨 💨 💨 \n')
    mainApp = MainController()

    try:
        mainApp.processArgs()
    except Exception as err:
        Logger.error("Error: {0}".format(err))
        traceback.print_exc()
