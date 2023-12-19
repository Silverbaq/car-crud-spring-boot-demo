# Car CRUD Spring boot demo

## Introduction
This is a demo project of a RESTapi in Spring Boot. It is showing CRUD operations, using cars as the example.

In this implementation, I choose not to use JPA as the communication to persisting data. Instead I choose to create a `FakeDB` to keep objects in memory.
For an example using JPA, switch to the branch `JPA-implementation`. 

## Run project

`./gradlew bootRun`

## Usage

### Get all cars

```bash
curl -X GET http://localhost:8080/api/v1/cars
```

### Get a car by `vin`

```bash
curl -X GET http://localhost:8080/api/v1/cars/WBA3R1C50EK192321
```

### Create a car

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "vin": "1G4HD57296U156210",
  "make": "Kia",
  "model": "Niro",
  "mileage": 101635.822,
  "year": 2008,
  "color": "Silver"
}' http://localhost:8080/api/v1/cars
```

### Update a car

```bash
curl -X PUT -H "Content-Type: application/json" -d '{
  "vin": "1G4HD57296U156210",
  "make": "Kia",
  "model": "Niro",
  "mileage": 101835.822,
  "year": 2008,
  "color": "Red"
}' http://localhost:8080/api/v1/cars/1G4HD57296U156210
```

### Delete a car

```bash
curl -X DELETE http://localhost:8080/api/v1/cars/1G4HD57296U156210
```
