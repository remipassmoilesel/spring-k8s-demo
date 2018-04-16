#!/usr/bin/env bash

# You can modify environment vars to modify site behavior

export K8S_DEMO_ACTIVE_PROFILE=dev

. setenv-dev.sh

./gradlew bootRun
