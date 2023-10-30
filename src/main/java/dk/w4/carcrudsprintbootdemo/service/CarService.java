package dk.w4.carcrudsprintbootdemo.service;

import dk.w4.carcrudsprintbootdemo.model.Car;
import dk.w4.carcrudsprintbootdemo.repositories.CarRepository;
import dk.w4.carcrudsprintbootdemo.util.VINValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final VINValidator vinValidator;

    @Autowired
    public CarService(CarRepository carRepository, VINValidator vinValidator) {
        this.carRepository = carRepository;
        this.vinValidator = vinValidator;
    }

    public Optional<Car> getCar(String vin) {
        return carRepository.findById(vin);
    }

    public Iterable<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car addCar(Car value) {
        if (vinValidator.isValidVIN(value.getVin())) {
            long currentUnitTimeStamp = System.currentTimeMillis() / 1000;
            value.setCreatedAt(currentUnitTimeStamp);
            return carRepository.saveAndFlush(value);
        }
        return null;
    }


    public Car updateCar(String vin, Car value) {
        if (!Objects.equals(vin, value.getVin())) return null;

        Car car = carRepository.findById(vin).orElse(null);
        if (car == null) return null;

        long currentUnitTimeStamp = System.currentTimeMillis() / 1000;
        car.setMake(value.getMake());
        car.setModel(value.getModel());
        car.setYear(value.getYear());
        car.setColor(value.getColor());
        car.setMileage(value.getMileage());
        car.setUpdatedAt(currentUnitTimeStamp);

        return carRepository.saveAndFlush(car);
    }

    public boolean deleteCar(String vin) {
        Car car = carRepository.findById(vin).orElse(null);
        if (car == null) return false;
        carRepository.delete(car);
        return true;
    }
}
