#!/bin/bash

# Get an access token from Keycloak
ACCESS_TOKEN=$(curl -s -X POST "$OAUTH_ISSUER_URI/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=$OAUTH_CLIENT_ID" \
  -d "client_secret=$OAUTH_CLIENT_SECRET" \
  -d "username=$KEYCLOAK_INITIAL_USERNAME" \
  -d "password=$KEYCLOAK_INITIAL_PASSWORD" \
  -d "grant_type=password" | grep -o '"access_token":"[^"]*"' | awk -F '\"' '{print $4}')

# Check if we got the token
if [ -z "$ACCESS_TOKEN" ]; then
  echo "Failed to obtain access token"
  exit 1
fi

# Perform the health check with the token
curl -f -H "Authorization: Bearer $ACCESS_TOKEN" http://localhost:8080/api/actuator/health || exit 1
