# coding: utf-8

from Config import Config
from commons.Logger import Logger
from commons.Shell import Shell


class DashboardService:

    def showDashboard(self):
        self.__showBanner()
        self.__showDashboardSecret()
        self.__openDashboard()
        self.__openProxy()

    @staticmethod
    def __showBanner():
        print("""
            
      ____            _     _                         _ 
     |  _ \  __ _ ___| |__ | |__   ___   __ _ _ __ __| |
     | | | |/ _` / __| '_ \| '_ \ / _ \ / _` | '__/ _` |
     | |_| | (_| \__ \ | | | |_) | (_) | (_| | | | (_| |
     |____/ \__,_|___/_| |_|_.__/ \___/ \__,_|_|  \__,_|
                                                        
    
        """)

    @staticmethod
    def __showDashboardSecret():
        Shell.run("kubectl describe secret eks-admin-token-dx9vk -n kube-system", cwd=Config.PROJECT_ROOT)

    @staticmethod
    def __openDashboard():
        Logger.info("\n\n" +
                    "Opening dashboard at: http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/"
                    + "\n\n")

        Shell.run(
            "xdg-open http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/ &",
            cwd=Config.PROJECT_ROOT)

    @staticmethod
    def __openProxy():
        Shell.run("kubectl proxy", cwd=Config.PROJECT_ROOT)
