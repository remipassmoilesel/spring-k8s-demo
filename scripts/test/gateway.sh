#!/usr/bin/env bash

source ./applications/gateway/setenv-test.sh

./gradlew gateway:test --info