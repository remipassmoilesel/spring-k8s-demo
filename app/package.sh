#!/usr/bin/env bash

cd frontend/

npm run update-static-dir

cd ..

./gradlew build -x test