#!/bin/bash

# mardi 1 mai 2018, 22:29:51 (UTC+0200)

# This script should be launch from upper directory

python3 -m unittest discover -s scripts/tests -p '*.py'
