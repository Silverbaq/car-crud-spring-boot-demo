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
        if (vinValidator.isValidVIN(value.vin())) {
            long currentUnitTimeStamp = System.currentTimeMillis() / 1000;
            Car car = value.setCreatedAt(currentUnitTimeStamp);
            if (carRepository.add(car)) return car;
            else return null;
        }
        return null;
    }

    public Car updateCar(String vin, Car value) {
        if (!Objects.equals(vin, value.vin())) return null;

        long currentUnitTimeStamp = System.currentTimeMillis() / 1000;
        Car car = value.setUpdatedAt(currentUnitTimeStamp);
        if (carRepository.update(vin, car)) return car;
        else return null;
    }

    public boolean deleteCar(String vin) {
        return carRepository.delete(vin);
    }
}
