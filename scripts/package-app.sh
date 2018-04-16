#!/bin/bash

# lundi 16 avril 2018, 20:44:19 (UTC+0200)

. ./scripts/setenv-dev.sh

cd app/frontend/

npm run update-static-dir

cd ..

./gradlew build -x test