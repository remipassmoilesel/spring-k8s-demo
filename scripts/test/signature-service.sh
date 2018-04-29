#!/usr/bin/env bash

source ./applications/signature-service/setenv-dev.sh

./gradlew signature-service:test --info