#!/bin/bash

docker network create adm_videos_services
docker network create elastic

mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak
mkdir -m 777 .docker/es01

docker compose -f services/docker-compose.yml up -d
docker compose -f elk/docker-compose.yml up -d

echo "Starting containers..."
sleep 20