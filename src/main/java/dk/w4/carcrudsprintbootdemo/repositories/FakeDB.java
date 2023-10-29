package dk.w4.carcrudsprintbootdemo.repositories;

import dk.w4.carcrudsprintbootdemo.model.Car;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FakeDB {
    private final List<Car> cars = Collections.synchronizedList(initialize());

    public boolean add(Car value) {
        if (cars.stream().filter(car -> Objects.equals(car.vin(), value.vin())).findFirst().isEmpty()) {
            return cars.add(value);
        }
        return false;
    }

    public boolean delete(Car value) {
        return cars.remove(value);
    }

    public boolean update(Car oldValue, Car newValue) {
        if (!cars.contains(oldValue)) return false;
        cars.remove(oldValue);
        cars.add(newValue);
        return true;
    }

    public Car findById(String id) {
        return cars.stream().filter(car -> Objects.equals(car.vin(), id)).findFirst().orElse(null);
    }

    public Iterable<Car> findAll() {
        return cars;
    }

    private ArrayList<Car> initialize() {
        return new ArrayList<>(Arrays.asList(
                new Car("WBA3R1C50EK192321", "Toyota", "Corolla", 84320.6922, 1993, "Red", 1698576654, 1698576654),
                new Car("5FNYF185X5B024676", "BMW", "3-Serie Sedan", 35232.36797, 2019, "Black", 1698576654, 1698576654),
                new Car("1ZVFT80N255145737", "Alfa Romeo", "Giulia", 97369.48719, 2008, "Blue", 1698576654, 1698576654)
        ));
    }
}
