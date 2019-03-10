#!/usr/bin/env bash

set -x
set -e

cd /sources

source ./applications/signature-service/setenv-docker.sh

java -jar ./applications/signature-service/build/libs/signature-service-0.0.1-SNAPSHOT.jar