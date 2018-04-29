#!/usr/bin/env bash

set -x
set -e

cd /sources

. ./applications/signature-service/setenv-docker.sh

./gradlew signature-service:bootRun