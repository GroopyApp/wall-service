# Start with a base image containing Java runtime
FROM openjdk:17

# Add Maintainer Info

# Add a volume pointing to /tmp
VOLUME /tmp

EXPOSE 8090

RUN mkdir /app
WORKDIR /app
COPY . .
RUN ./gradlew clean build


ENTRYPOINT ["java","-jar","./build/libs/room-service-1.0.jar", "-spring.profiles.active=prod" ,"--spring.config.location=classpath:/docker.yaml"]