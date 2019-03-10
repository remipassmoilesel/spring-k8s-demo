#!/usr/bin/env bash

set -x
set -e

cd /sources

source ./applications/gateway/setenv-docker.sh

java -jar ./applications/gateway/build/libs/gateway-0.0.1-SNAPSHOT.jar