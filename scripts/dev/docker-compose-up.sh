#!/usr/bin/env bash

./gradlew build -x test

cd docker-compose-dev
docker-compose up