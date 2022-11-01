# Getting Started

## Using terminal

### Build the project
gradlew build
gradlew clean build

### Running the microservice processes
java -jar microservices\tollfee-service\build\libs\tollfee-service-1.0.0-SNAPSHOT.jar &

java -jar microservices\tollfeecalculator-service\build\libs\tollfeecalculator-service-1.0.0-SNAPSHOT.jar

java -jar microservices\tollfee-composite-service\build\libs\tollfee-composite-service-1.0.0-SNAPSHOT.jar

### Test the application
curl http://localhost:7001/tollfee/gothenburg

http://localhost:7000/swagger-ui/index.html

## Docker

### Build
gradlew clean build && docker-compose build

### Running the docker containers
docker-compose up -d

### Closing
docker-compose down

### Test the application
http://localhost:8080/swagger-ui/index.html

### Prune docker images
docker system prune -f --volumes

## Database

Database mongoDB database where the data is loaded from a json file located here
\\CongestionTaxCalculator\microservices\tollfee-service\src\main\resources\data\data.json

This file can be modified to add more cities and new tax rules
