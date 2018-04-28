#!/usr/bin/env bash

source ./scripts/setenv-dev.sh

sudo mysql -u root -e "CREATE DATABASE $K8SDEMO_DBNAME"
sudo mysql -u root -e "CREATE DATABASE $K8SDEMO_DBNAME_DEV"
sudo mysql -u root -e "CREATE USER '$K8SDEMO_DB_USERNAME'@'$K8SDEMO_DB_HOST' IDENTIFIED BY '$K8SDEMO_DB_PASSWORD';"

sudo mysql -u root -e "GRANT ALL ON $K8SDEMO_DBNAME.* TO '$K8SDEMO_DB_USERNAME'@'$K8SDEMO_DB_HOST';"
sudo mysql -u root -e "GRANT ALL ON $K8SDEMO_DBNAME_DEV.* TO '$K8SDEMO_DB_USERNAME'@'$K8SDEMO_DB_HOST';"
