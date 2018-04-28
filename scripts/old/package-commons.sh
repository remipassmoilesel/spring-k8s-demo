#!/bin/bash

# lundi 16 avril 2018, 20:44:19 (UTC+0200)

. ./scripts/setenv-dev.sh

./gradlew -x test -Dorg.gradle.warning.mode=all :microservice_commons:build
