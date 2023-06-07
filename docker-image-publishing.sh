docker build --tag wall-service:latest --platform=linux/amd64 .
docker tag wall-service:latest aledanna/wall-service
docker push aledanna/wall-service