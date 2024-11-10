#!/bin/sh

export $(grep -v '^#' .env | xargs)

mvn spring-boot:run -Dspring.profiles.active=local