#!/bin/bash

docker network create adm_videos_services

mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak

docker compose -f services/docker-compose.yml up

echo "Starting containers..."
sleep 20