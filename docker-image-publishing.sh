docker build --tag room-service:latest --platform=linux/amd64 .
docker tag room-service:latest aledanna/room-service
docker push aledanna/room-service