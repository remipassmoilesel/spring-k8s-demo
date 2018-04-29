#!/usr/bin/env bash

source ./applications/signature-service/setenv-dev.sh

./gradlew microservice-commons:test --info --rerun-tasks