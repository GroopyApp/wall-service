# wall-service

A java microservice to manage wall operations in Groopy system

## Running

### Running as a java process
To run the project locally as a java process you must run before [infrastructure](https://github.com/GroopyApp/infrastructure) docker container in order to have all the dependencies you need running.

#### Environment variables
You need to have the following environment variables set up in order to run the service
- **MONGO_DB_URI** (The connection string to your local mongoDB db instance)

_You can do it replacing the values in project .env file_

### Running as a docker container
Running the project with docker allow us to create a complete environment, where we can also attach other services such as [user-service](https://github.com/GroopyApp/user-service)
To run or attach wall-service to docker environment you simply must run:

`sh ./docker-run.sh`
