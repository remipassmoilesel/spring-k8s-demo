#!/usr/bin/env bash

# You can modify these environment vars to modify application behavior

export SIGNATURE_SERV_ACTIVE_PROFILE=dev

export SIGNATURE_SERV_DB_NAME=documents

export SIGNATURE_SERV_DB_URI=mongodb://mongodb.docker.net:27017
export SIGNATURE_SERV_DB_USERNAME=k8sdemo
export SIGNATURE_SERV_DB_PASSWORD=0ddc32bdff76469a8cc9821b53792e20dad7c9570d8a47be8473f414d32ca89a77916ad292dfceb4319007bc5a53d0b8f5792ddbb7eedf9

export MICROCOMM_CONTEXT=k8sdemo
export MICROCOMM_NATS_URL=nats://nats.docker.net:4222