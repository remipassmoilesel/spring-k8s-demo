#!/usr/bin/env bash

set -x
set -e

cd /sources

. ./applications/gateway/setenv-docker.sh

./gradlew gateway:bootRun