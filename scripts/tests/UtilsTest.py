# -*- coding: utf-8 -*-
import unittest

from scripts.Utils import Utils
from scripts.Containers import Container

testContainer1 = Container(serviceName="fake-service1",
                           devEnvFile="./applications/gateway/setenv-dev.sh",
                           dockerBuildDir="./applications/gateway",
                           imageName="docker.remi-pace.fr/k8sdemo.gateway:0.1")

testContainer2 = Container(serviceName="fake-service2",
                          devEnvFile="./applications/gateway/setenv-dev.sh",
                          dockerBuildDir="./applications/gateway",
                          imageName="docker.remi-pace.fr/k8sdemo.gateway:0.1")

class TestStringMethods(unittest.TestCase):

    def test_joinGradleAppNames(self):
        namesWithTasks = Utils.joinGradleAppNames([testContainer1, testContainer2], 'test')
        self.assertEqual(namesWithTasks, 'fake-service1:test fake-service2:test')
