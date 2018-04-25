#!/bin/bash

# lundi 16 avril 2018, 20:44:19 (UTC+0200)

. ./scripts/setenv-dev.sh

# TODO: build frontend

./gradlew build -x test -Dorg.gradle.warning.mode=all
