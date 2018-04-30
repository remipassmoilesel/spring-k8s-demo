# -*- coding: utf-8 -*-
from os.path import *


class Paths:
    ROOT = abspath(join(__file__, '..', '..'))
    DOCKER_COMPOSE_ROOT = join(ROOT, 'docker-compose-dev')
    FRONTEND_ROOT = join(ROOT, 'applications', 'frontend')


assert(isdir(Paths.ROOT))
assert(isfile(join(Paths.ROOT, 'README.md')))
assert(isdir(Paths.DOCKER_COMPOSE_ROOT))
