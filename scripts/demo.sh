#!/usr/bin/env bash

cd applications/frontend
npm run update-gateway
cd ../..

./gradlew build -x test

cd docker-compose-dev
docker-compose up