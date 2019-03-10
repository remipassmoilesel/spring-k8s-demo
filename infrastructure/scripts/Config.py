# -*- coding: utf-8 -*-
import os
from os.path import *
from typing import List

from commons.DockerImage import DockerImage


class Config:
    DOCKER_TAG = "0.1"  # That's very bad, use Git SHA instead :)
    PROJECT_ROOT = abspath(join(__file__, '..', '..', '..'))
    DOCKER_COMPOSE_ROOT = join(PROJECT_ROOT, 'infrastructure/docker-compose-dev')
    FRONTEND_ROOT = join(PROJECT_ROOT, 'applications/frontend')
    HELM_CHART_PATH = join(PROJECT_ROOT, 'infrastructure/helm-chart')

    @staticmethod
    def getDockeRegistryPrefix():
        return "registry.gitlab.com/remipassmoilesel/spring-k8s-demo"

    @staticmethod
    def getBaseDockerImage() -> DockerImage:
        return DockerImage('k8sdemo.base-image',
                           os.path.join(Config.PROJECT_ROOT, 'infrastructure/base-docker-image'),
                           Config.getDockeRegistryPrefix())

    @staticmethod
    def getDockerImages() -> List[DockerImage]:
        return [
            DockerImage("gateway",
                        os.path.join(Config.PROJECT_ROOT, "applications/gateway"),
                        Config.getDockeRegistryPrefix()),
            DockerImage("signature-service",
                        os.path.join(Config.PROJECT_ROOT, "applications/signature-service"),
                        Config.getDockeRegistryPrefix()),
        ]


assert (isdir(Config.PROJECT_ROOT))
assert (isfile(join(Config.PROJECT_ROOT, 'README.md')))
assert (isdir(Config.DOCKER_COMPOSE_ROOT))
