#!/usr/bin/env bash

. ./applications/signature-service/setenv-dev.sh

./gradlew signature-service:test --info