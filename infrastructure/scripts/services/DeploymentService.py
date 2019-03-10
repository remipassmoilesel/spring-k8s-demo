# -*- coding: utf-8 -*-
import os

from Config import Config
from commons.Logger import Logger
from commons.Shell import Shell
from services.BuildService import BuildService


class DeploymentService:

    def __init__(self):
        self.buildService = BuildService()

    def __showDeploymentBanner(self):
        Logger.info("""    
            
    ██╗███╗   ███╗███╗   ███╗██╗███╗   ██╗███████╗███╗   ██╗████████╗    ████████╗ █████╗ ██╗  ██╗███████╗     ██████╗ ███████╗███████╗    ██╗
    ██║████╗ ████║████╗ ████║██║████╗  ██║██╔════╝████╗  ██║╚══██╔══╝    ╚══██╔══╝██╔══██╗██║ ██╔╝██╔════╝    ██╔═══██╗██╔════╝██╔════╝    ██║
    ██║██╔████╔██║██╔████╔██║██║██╔██╗ ██║█████╗  ██╔██╗ ██║   ██║          ██║   ███████║█████╔╝ █████╗      ██║   ██║█████╗  █████╗      ██║
    ██║██║╚██╔╝██║██║╚██╔╝██║██║██║╚██╗██║██╔══╝  ██║╚██╗██║   ██║          ██║   ██╔══██║██╔═██╗ ██╔══╝      ██║   ██║██╔══╝  ██╔══╝      ╚═╝
    ██║██║ ╚═╝ ██║██║ ╚═╝ ██║██║██║ ╚████║███████╗██║ ╚████║   ██║          ██║   ██║  ██║██║  ██╗███████╗    ╚██████╔╝██║     ██║         ██╗
    ╚═╝╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═══╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝     ╚═════╝ ╚═╝     ╚═╝         ╚═╝
                                                                                                                                              
        """)

    def showDestroyBanner(self):
        Logger.error("""        
            
    ██╗███╗   ███╗███╗   ███╗██╗███╗   ██╗███████╗███╗   ██╗████████╗     ██████╗██████╗  █████╗ ███████╗██╗  ██╗    ██╗
    ██║████╗ ████║████╗ ████║██║████╗  ██║██╔════╝████╗  ██║╚══██╔══╝    ██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║    ██║
    ██║██╔████╔██║██╔████╔██║██║██╔██╗ ██║█████╗  ██╔██╗ ██║   ██║       ██║     ██████╔╝███████║███████╗███████║    ██║
    ██║██║╚██╔╝██║██║╚██╔╝██║██║██║╚██╗██║██╔══╝  ██║╚██╗██║   ██║       ██║     ██╔══██╗██╔══██║╚════██║██╔══██║    ╚═╝
    ██║██║ ╚═╝ ██║██║ ╚═╝ ██║██║██║ ╚████║███████╗██║ ╚████║   ██║       ╚██████╗██║  ██║██║  ██║███████║██║  ██║    ██╗
    ╚═╝╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═══╝   ╚═╝        ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝    ╚═╝
                                                                                                                    
        """)

    def deploy(self, environmentName: str) -> None:
        self.__showDeploymentBanner()

        prefixedEnv = self.__getPrefixedEnvironmentName(environmentName)
        self.__createNamespace(prefixedEnv)
        self.__createRegistrySecret(prefixedEnv)
        self.__deployHelmChart(prefixedEnv)

    def destroy(self, environmentName):
        self.showDestroyBanner()

        prefixedEnv = self.__getPrefixedEnvironmentName(environmentName)
        self.__destroyHelmChart(prefixedEnv)
        self.__destroyNamespace(prefixedEnv)

    def __createNamespace(self, namespace):
        try:
            Shell.run("kubectl create namespace " + namespace)
        except:
            Logger.error('Error while creating namespace')

    def __createRegistrySecret(self, namespace):
        createSecret = "kubectl create secret generic registry-secret"
        createSecret += " --from-file=.dockerconfigjson=" + os.path.expanduser('~/.docker/config.json')
        createSecret += " --type=kubernetes.io/dockerconfigjson "
        createSecret += " --namespace " + namespace
        try:
            Shell.run(createSecret)
        except:
            Logger.error('Error while creating registry secret')

    def __deployHelmChart(self, environmentName):
        helmUpgrade = "helm upgrade "
        helmUpgrade += " --install --force " + environmentName
        helmUpgrade += " --wait --timeout 500"
        helmUpgrade += " --namespace " + environmentName
        helmUpgrade += " " + Config.HELM_CHART_PATH
        Shell.run(helmUpgrade)

    def __destroyHelmChart(self, environmentName):
        Shell.run("helm delete --purge " + environmentName)

    def __destroyNamespace(self, namespace):
        try:
            Shell.run("kubectl delete namespace " + namespace)
        except:
            Logger.error('Error while deleting namespace')

    def __getPrefixedEnvironmentName(self, env: str) -> str:
        return "skd-" + env
