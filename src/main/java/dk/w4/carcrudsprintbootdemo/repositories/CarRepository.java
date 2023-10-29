package dk.w4.carcrudsprintbootdemo.repositories;

import dk.w4.carcrudsprintbootdemo.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class CarRepository implements IRepository<Car, String> {
    private final FakeDB database;

    @Autowired
    CarRepository(FakeDB database) {
        this.database = database;
    }

    @Override
    public boolean add(Car value) {
        return database.add(value);
    }

    @Override
    public boolean delete(String id) {
        Car value = database.findById(id);
        if (value == null) return false;
        return database.delete(value);
    }

    @Override
    public boolean update(String id, Car value) {
        Car oldCar = database.findById(id);
        return database.update(oldCar, value);
    }

    @Override
    public Optional<Car> findById(String vin) {
        return Optional.ofNullable(database.findById(vin));
    }

    @Override
    public Iterable<Car> findAll() {
        return database.findAll();
    }
}
