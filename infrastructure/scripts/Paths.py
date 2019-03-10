# -*- coding: utf-8 -*-
from os.path import *


class Paths:
    ROOT = abspath(join(__file__, '..', '..', '..'))
    DOCKER_COMPOSE_ROOT = join(ROOT, 'infrastructure/docker-compose-dev')
    FRONTEND_ROOT = join(ROOT, 'applications', 'frontend')
    HELM_CHART_PATH = join(ROOT, 'infrastructure/helm-chart')


assert(isdir(Paths.ROOT))
assert(isfile(join(Paths.ROOT, 'README.md')))
assert(isdir(Paths.DOCKER_COMPOSE_ROOT))
