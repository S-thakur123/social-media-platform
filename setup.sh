#!/bin/bash

# Colors for better visibility
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "🚀 Starting Social Platform 2026 Pre-flight Checks..."

# 1. Check if .env exists
if [ -f .env ]; then
    echo -e "${GREEN}✅ .env file found.${NC}"
else
    echo -e "${RED}❌ Error: .env file is missing!${NC}"
    echo "Please copy .env.example to .env and fill in your secrets."
    exit 1
fi

# 2. Check for critical variables in .env
REQUIRED_VARS=("JWT_SECRET" "MAIL_PASSWORD" "DB_PASS")
for var in "${REQUIRED_VARS[@]}"; do
    if ! grep -q "$var" .env || grep -q "$var=$" .env; then
        echo -e "${RED}❌ Error: $var is not set in .env${NC}"
        exit 1
    fi
done
echo -e "${GREEN}✅ All critical environment variables are set.${NC}"

# 3. Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Error: Docker is not running!${NC}"
    echo "Please start Docker Desktop or the Docker daemon."
    exit 1
fi
echo -e "${GREEN}✅ Docker is running.${NC}"

# 4. Check for existing Docker network (optional but helpful)
NETWORK_NAME="social_network"
if ! docker network ls | grep -q "$NETWORK_NAME"; then
    echo "🌐 Creating docker network: $NETWORK_NAME"
    docker network create $NETWORK_NAME
fi

# 5. Launch
echo -e "${GREEN}✨ Everything looks good! Launching containers...${NC}"
docker compose up -d --build

echo "-------------------------------------------------------"
echo "Services are starting up. Use 'docker compose logs -f' to watch."
echo "Eureka Dashboard: http://localhost:8761"
echo "Grafana: http://localhost:3000"
echo "-------------------------------------------------------"