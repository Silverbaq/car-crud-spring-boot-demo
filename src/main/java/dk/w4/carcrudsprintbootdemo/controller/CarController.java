package dk.w4.carcrudsprintbootdemo.controller;

import dk.w4.carcrudsprintbootdemo.exception.ResourceNotFoundException;
import dk.w4.carcrudsprintbootdemo.model.Car;
import dk.w4.carcrudsprintbootdemo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public ResponseEntity<Iterable<Car>> getAllCars() {
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }

    @GetMapping("/cars/{vin}")
    public ResponseEntity<Car> getCar(@PathVariable(value = "vin") String vin) {
        return new ResponseEntity<>(carService.getCar(vin).orElseThrow(() -> new ResourceNotFoundException("Car", "vin", vin)), HttpStatus.OK);
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car result = carService.addCar(car);
        return result != null ? new ResponseEntity<>(result, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/cars/{vin}")
    public ResponseEntity<Car> updateCar(@PathVariable(value = "vin") String vin, @RequestBody Car car) {
        Car result = carService.updateCar(vin, car);
        return result != null ? new ResponseEntity<>(result, HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/cars/{vin}")
    public ResponseEntity<String> deleteCar(@PathVariable(value = "vin") String vin) {
        boolean result = carService.deleteCar(vin);
        return result ? new ResponseEntity<>(vin, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
