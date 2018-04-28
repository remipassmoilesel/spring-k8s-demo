#!/usr/bin/env bash

# lundi 16 avril 2018, 20:44:19 (UTC+0200)

./scripts/package-app.sh

docker build . -t spring-k8s-demo:0.1