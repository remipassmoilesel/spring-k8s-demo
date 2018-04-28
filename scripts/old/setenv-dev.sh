#!/usr/bin/env bash

# You can modify these environment vars to modify application behavior

export K8SDEMO_ACTIVE_PROFILE=dev

export K8SDEMO_DBNAME=k8sdemo
export K8SDEMO_DBNAME_DEV=k8sdemo_dev

# Specify JDBC url without database name
export K8SDEMO_DB_HOST=localhost
export K8SDEMO_DB_URL=jdbc:mysql://$K8SDEMO_DB_HOST:3306/
export K8SDEMO_DB_USERNAME=k8sdemo
export K8SDEMO_DB_PASSWORD=0ddc32bdff76469a8cc9821b53792e20dad7c9570d8a47be8473f414d32ca89a77916ad292dfceb4319007bc5a53d0b8f5792ddbb7eedf9

export K8SDEMO_LOGIN=admin
export K8SDEMO_PASSWORD=admin


