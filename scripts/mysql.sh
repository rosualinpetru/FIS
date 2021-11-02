#!/bin/bash

SCRIPT_DIR="$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

docker run \
  -p 3306:3306 \
  -e "MYSQL_ROOT_PASSWORD=$1" \
  -e "MYSQL_DATABASE=rhs_tomapp" \
  -e "MYSQL_USER=tomapp" \
  -e "MYSQL_PASSWORD=$1" \
  -v "$SCRIPT_DIR/data:/var/lib/mysql" \
  -d mysql:8.0.26