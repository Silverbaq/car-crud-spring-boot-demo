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
        when(carRepository.findById(car.getVin())).thenReturn(Optional.of(car));

        Car actual = carService.getCar(car.getVin()).orElse(null);

        assertEquals(car, actual);
    }

    @Test
    void getCar_carWithVinDoesNotExists_returnsNull() {
        when(carRepository.findById(car.getVin())).thenReturn(Optional.ofNullable(null));

        Car actual = carService.getCar(car.getVin()).orElse(null);

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
        when(vinValidator.isValidVIN(car.getVin())).thenReturn(true);
        when(carRepository.saveAndFlush(car)).thenReturn(car);

        Car actual = carService.addCar(car);

        assertEquals(car, actual);

        verify(vinValidator, times(1)).isValidVIN(car.getVin());
        verify(carRepository, times(1)).saveAndFlush(car);
    }

    @Test
    void createCar_carIsCreated_createdAtValueIsSet() {
        Car car = new Car("WBA3R1C50EK192321", "Toyota", "Corolla", 84320.6922, 1993, "Red", 0L, 0L);

        when(vinValidator.isValidVIN(car.getVin())).thenReturn(true);
        when(carRepository.saveAndFlush(car)).thenReturn(car);

        Car actual = carService.addCar(car);

        assertNotEquals(car.getCreatedAt(), actual.getCreatedAt());
        assertNotEquals(car.getUpdatedAt(), actual.getUpdatedAt());

        verify(vinValidator, times(1)).isValidVIN(car.getVin());
        verify(carRepository, times(1)).saveAndFlush(car);
    }

    @Test
    void createCar_invalidVIN_nullIsReturned() {
        when(vinValidator.isValidVIN(car.getVin())).thenReturn(false);

        Car actual = carService.addCar(car);

        assertNull(actual);

        verify(vinValidator, times(1)).isValidVIN(car.getVin());
        verify(carRepository, times(0)).saveAndFlush(car);
    }

    @Test
    void createCar_carNotAccepted_nullIsReturned() {
        when(vinValidator.isValidVIN(car.getVin())).thenReturn(true);
        when(carRepository.saveAndFlush(car)).thenReturn(null);

        Car actual = carService.addCar(car);

        assertNull(actual);

        verify(vinValidator, times(1)).isValidVIN(car.getVin());
        verify(carRepository, times(0)).saveAndFlush(car);
    }


    @Test
    void updateCar_validValues_updatedCarIsReturned() {
        when(carRepository.findById(car.getVin())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(car)).thenReturn(car);

        var updatedCar = new Car(car.getVin(), car.getMake(), car.getModel(), car.getMileage(), car.getYear(), "new-color", car.getCreatedAt(), 0L);

        Car actual = carService.updateCar(car.getVin(), updatedCar);

        assertEquals(updatedCar.getColor(), actual.getColor());

        verify(carRepository, times(1)).saveAndFlush(car);
    }


    @Test
    void updateCar_carIsUpdated_updatedAtValueIsUpdated() {
        when(carRepository.findById(car.getVin())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(car)).thenReturn(car);

        Car updatedCar = new Car(car.getVin(), car.getMake(), car.getModel(), car.getMileage(), car.getYear(), "new-color", car.getCreatedAt(), 0L);
        Car actual = carService.updateCar(car.getVin(), updatedCar);

        assertNotEquals(updatedCar.getUpdatedAt(), actual.getUpdatedAt());

        verify(carRepository, times(1)).saveAndFlush(car);
    }

    @Test
    void updateCar_vinAndCarVinAreNotTheSame_returnedNull() {
        String differentVin = "1ZVFT80N255145737";
        Car updatedCar = new Car(car.getVin(), car.getMake(), car.getModel(), car.getMileage(), car.getYear(), "new-color", car.getCreatedAt(), 0L);
        Car actual = carService.updateCar(differentVin, updatedCar);

        assertNull(actual);

        verify(carRepository, times(0)).saveAndFlush(car);
    }

    @Test
    void updateCar_carIsDoesNotExist_returnedNull() {
        when(carRepository.findById(car.getVin())).thenReturn(Optional.ofNullable(null));

        Car updatedCar = new Car(car.getVin(), car.getMake(), car.getModel(), car.getMileage(), car.getYear(), "new-color", car.getCreatedAt(), 0L);
        Car actual = carService.updateCar(car.getVin(), updatedCar);

        assertNull(actual);

        verify(carRepository, times(0)).saveAndFlush(car);
    }

    @Test
    void deleteCar_carIsDeleted_returnedTrue() {
        when(carRepository.findById(car.getVin())).thenReturn(Optional.ofNullable(car));

        boolean actual = carService.deleteCar(car.getVin());

        assertTrue(actual);
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void deleteCar_carDoesNotExist_returnedFalse() {
        when(carRepository.findById(car.getVin())).thenReturn(Optional.ofNullable(null));

        boolean actual = carService.deleteCar(car.getVin());

        assertFalse(actual);
        verify(carRepository, times(0)).delete(car);
    }
}
