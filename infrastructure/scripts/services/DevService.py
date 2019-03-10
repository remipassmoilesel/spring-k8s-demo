# -*- coding: utf-8 -*-
from Config import Config
from commons.Logger import Logger
from commons.Shell import Shell


class DevController:

    def startAll(self) -> None:
        self.__showBanner()
        return self.dockerComposeStart()

    def dockerComposeStart(self) -> None:
        return Shell.run("docker-compose up -d ", Config.DOCKER_COMPOSE_ROOT)

    def __showBanner(self) -> None:
        Logger.success("""        
    ______           __                                                          __  __         ____      
   / ____/___ ______/ /____  ____     __  ______  __  _______   ________  ____ _/ /_/ /_  ___  / / /______
  / /_  / __ `/ ___/ __/ _ \/ __ \   / / / / __ \/ / / / ___/  / ___/ _ \/ __ `/ __/ __ \/ _ \/ / __/ ___/
 / __/ / /_/ (__  ) /_/  __/ / / /  / /_/ / /_/ / /_/ / /     (__  )  __/ /_/ / /_/ /_/ /  __/ / /_(__  ) 
/_/    \__,_/____/\__/\___/_/ /_/   \__, /\____/\__,_/_/     /____/\___/\__,_/\__/_.___/\___/_/\__/____/  
                                   /____/                                                                 

        """)
