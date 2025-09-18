#!/bin/bash
FILENAME=$@
docker stack deploy -c $FILENAME opensign
