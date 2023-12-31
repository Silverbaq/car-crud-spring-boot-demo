package dk.w4.carcrudsprintbootdemo.services;

import dk.w4.carcrudsprintbootdemo.model.Car;
import dk.w4.carcrudsprintbootdemo.repositories.CarRepository;
import dk.w4.carcrudsprintbootdemo.service.CarService;
import dk.w4.carcrudsprintbootdemo.util.VINValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private CarRepository carRepository = mock(CarRepository.class);

    private VINValidator vinValidator = mock(VINValidator.class);

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService(carRepository, vinValidator);
    }

    private Car car = new Car("WBA3R1C50EK192321", "Toyota", "Corolla", 84320.6922, 1993, "Red", 1698576654, 1698576654);

    @Test
    void getCar_carWithVinExists_returnsCar() {
        when(carRepository.findById(car.vin())).thenReturn(Optional.of(car));

        Car actual = carService.getCar(car.vin()).orElse(null);

        assertEquals(car, actual);
    }

    @Test
    void getCar_carWithVinDoesNotExists_returnsNull() {
        when(carRepository.findById(car.vin())).thenReturn(Optional.ofNullable(null));

        Car actual = carService.getCar(car.vin()).orElse(null);

        assertNull(actual);
    }

    @Test
    void getAllCars_carsAreAvailable_returnsAllCars() {
        List<Car> carsList = new ArrayList<>(Arrays.asList(car, car, car));
        when(carRepository.findAll()).thenReturn(carsList);

        Iterable<Car> actual = carService.getAllCars();

        assertEquals(carsList, actual);
    }

    @Test
    void createCar_validValues_carIsReturned() {
        when(vinValidator.isValidVIN(car.vin())).thenReturn(true);
        when(carRepository.add(car)).thenReturn(true);

        Car actual = carService.addCar(car);

        assertEquals(car, actual);

        verify(vinValidator, times(1)).isValidVIN(car.vin());
        verify(carRepository, times(1)).add(car);
    }

    @Test
    void createCar_carIsCreated_createdAtValueIsSet() {
        Car car = new Car("WBA3R1C50EK192321", "Toyota", "Corolla", 84320.6922, 1993, "Red", 0L, 0L);

        when(vinValidator.isValidVIN(car.vin())).thenReturn(true);
        when(carRepository.add(car)).thenReturn(true);

        Car actual = carService.addCar(car);

        assertNotEquals(car.createdAt(), actual.createdAt());
        assertNotEquals(car.updatedAt(), actual.updatedAt());

        verify(vinValidator, times(1)).isValidVIN(car.vin());
        verify(carRepository, times(1)).add(car);
    }

    @Test
    void createCar_invalidVIN_nullIsReturned() {
        when(vinValidator.isValidVIN(car.vin())).thenReturn(false);

        Car actual = carService.addCar(car);

        assertNull(actual);

        verify(vinValidator, times(1)).isValidVIN(car.vin());
        verify(carRepository, times(0)).add(car);
    }

    @Test
    void createCar_carNotAccepted_nullIsReturned() {
        when(vinValidator.isValidVIN(car.vin())).thenReturn(true);
        when(carRepository.add(car)).thenReturn(false);

        Car actual = carService.addCar(car);

        assertNull(actual);

        verify(vinValidator, times(1)).isValidVIN(car.vin());
        verify(carRepository, times(0)).add(car);
    }

    @Test
    void updateCar_validValues_updatedCarIsReturned() {
        when(carRepository.update(any(), any())).thenReturn(true);

        var updatedCar = new Car(car.vin(), car.make(), car.model(), car.mileage(), car.year(), "new-color", car.createdAt(), 0L);
        Car actual = carService.updateCar(car.vin(), updatedCar);

        assertEquals(updatedCar.color(), actual.color());

        verify(carRepository, times(1)).update(car.vin(), actual);
    }

    @Test
    void updateCar_carIsUpdated_updatedAtValueIsUpdated() {
        when(carRepository.update(any(), any())).thenReturn(true);

        Car updatedCar = new Car(car.vin(), car.make(), car.model(), car.mileage(), car.year(), "new-color", car.createdAt(), 0L);
        Car actual = carService.updateCar(car.vin(), updatedCar);

        assertNotEquals(updatedCar.updatedAt(), actual.updatedAt());

        verify(carRepository, times(1)).update(car.vin(), actual);
    }

    @Test
    void updateCar_vinAndCarVinAreNotTheSame_returnedNull() {
        String differentVin = "1ZVFT80N255145737";
        Car updatedCar = new Car(car.vin(), car.make(), car.model(), car.mileage(), car.year(), "new-color", car.createdAt(), 0L);
        Car actual = carService.updateCar(differentVin, updatedCar);

        assertNull(actual);

        verify(carRepository, times(0)).update(car.vin(), updatedCar);
    }

    @Test
    void updateCar_carIsNotUpdated_returnedNull() {
        when(carRepository.update(car.vin(), car)).thenReturn(false);

        Car updatedCar = new Car(car.vin(), car.make(), car.model(), car.mileage(), car.year(), "new-color", car.createdAt(), 0L);
        Car actual = carService.updateCar(car.vin(), updatedCar);

        assertNull(actual);

        verify(carRepository, times(0)).update(car.vin(), updatedCar);
    }


    @Test
    void deleteCar_carIsDeleted_returnedTrue() {
        when(carRepository.delete(car.vin())).thenReturn(true);

        boolean actual = carService.deleteCar(car.vin());

        assertTrue(actual);

        verify(carRepository, times(1)).delete(car.vin());
    }

    @Test
    void deleteCar_carIsNotDeleted_returnedFalse() {
        when(carRepository.delete(car.vin())).thenReturn(false);

        boolean actual = carService.deleteCar(car.vin());

        assertFalse(actual);

        verify(carRepository, times(1)).delete(car.vin());
    }
}
