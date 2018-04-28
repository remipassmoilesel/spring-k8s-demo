#!/usr/bin/env bash

. ./applications/signature-service/setenv-dev.sh

./gradlew microservice-commons:test --info --rerun-tasks