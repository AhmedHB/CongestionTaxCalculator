# CongestionTaxCalculator

## Background
This application is about calculation the congestion tax fee for vehicles within different cities. Two methods exist. Either calculate a onetime fee or a daily fee for the congestion tax.

## Business rules
Each city has its own tax rules. A tax rule could be for example be that in one city some vehicles are tax free. Another rule could be a single charge rule, where if a vehicle passes several tolling stations within 60 minutes only one tax is applied. The tax that is chosen is the highest amount. Other rules could be weekends are free regardless of vehicle type and time. Finally, there is a maximum amount per day a vehicle can be charged and that is 60 SEK.

Here are some examples of vehicles that are toll fee
•	Emergency vehicles
•	Busses
•	Diplomat vehicles
•	Motorcycles
•	Military vehicles
•	Foreign vehicles

Here is an example of the hours and amounts for congestion tax in one city:

| Time        | Amount |
| ----------- | :----: |
| 06:00–06:29 | SEK 8  |
| 06:30–06:59 | SEK 13 |
| 07:00–07:59 | SEK 18 |
| 08:00–08:29 | SEK 13 |
| 08:30–14:59 | SEK 8  |
| 15:00–15:29 | SEK 13 |
| 15:30–16:59 | SEK 18 |
| 17:00–17:59 | SEK 13 |
| 18:00–18:29 | SEK 8  |
| 18:30–05:59 | SEK 0  |

## How to build and run the code

Using Gradle build tool and docker if you are at root directory of the project in a terminal you can run the following commands:
gradlew clean build && docker-compose build

docker-compose up -d

Once the application is running visit the following URL using a browser:

http://localhost:8080/swagger-ui/index.html

The result is a swagger documentation page where two apis can be tested over http.
