#!/bin/bash
mvn clean spotless:apply package && \
docker build -t infotekies/password-generator-service . && \
docker-compose -f docker-compose.passwordgen.yaml  down && \
docker-compose -f docker-compose.passwordgen.yaml  up -d
