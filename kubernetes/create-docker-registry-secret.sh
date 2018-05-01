#!/bin/bash

# mardi 1 mai 2018, 15:57:30 (UTC+0200)

# Note: you can display your password with command: $ cat ~/.docker/config.json


SECRET_NAME=registry-secret
SERVER=fillme
USERNAME=fillme
PASSWORD=fillme
EMAIL=fillme

kubectl create secret docker-registry $SECRET_NAME \
                        --docker-server=$SERVER \
                        --docker-username=$USERNAME \
                        --docker-password=$PASSWORD \
                        --docker-email=$EMAIL
