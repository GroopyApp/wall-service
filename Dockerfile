# Start with a base image containing Java runtime
FROM openjdk:17

# Add Maintainer Info

# Add a volume pointing to /tmp
VOLUME /tmp

EXPOSE 8090


RUN mkdir /app
WORKDIR /app
COPY . /app
RUN ./gradlew build


ENTRYPOINT ["java","-jar","./build/libs/room-service-1.0.jar","--spring.config.location=classpath:/docker.yaml"]