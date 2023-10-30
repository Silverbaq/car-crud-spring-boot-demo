package dk.w4.carcrudsprintbootdemo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.w4.carcrudsprintbootdemo.controller.CarController;
import dk.w4.carcrudsprintbootdemo.model.Car;
import dk.w4.carcrudsprintbootdemo.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarController.class)
@ActiveProfiles("test")
class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private Car car1 = new Car("WBA3R1C50EK192321", "Toyota", "Corolla", 84320.6922, 1993, "Red", 1698576654, 1698576654);
    private Car car2 = new Car("5FNYF185X5B024676", "BMW", "3-Serie Sedan", 35232.36797, 2019, "Black", 1698576654, 1698576654);
    private Car car3 = new Car("1ZVFT80N255145737", "Alfa Romeo", "Giulia", 97369.48719, 2008, "Blue", 1698576654, 1698576654);

    @Test
    void getAllCars_carsAreAvailable_allCarsAreReturned() throws Exception {
        List<Car> carsList = new ArrayList<>(Arrays.asList(car1, car2, car3));
        when(carService.getAllCars()).thenReturn(carsList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/cars").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(carsList.size()))
                .andExpect(jsonPath("$[0].vin").value(car1.getVin()))
                .andExpect(jsonPath("$[1].vin").value(car2.getVin()))
                .andExpect(jsonPath("$[2].vin").value(car3.getVin()));
    }

    @Test
    void getCar_carWithVinIsAvailable_carWithVinIsReturned() throws Exception {
        when(carService.getCar(car1.getVin())).thenReturn(Optional.ofNullable(car1));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/".concat(car1.getVin())).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value(car1.getVin()))
                .andExpect(jsonPath("$.make").value(car1.getMake()))
                .andExpect(jsonPath("$.model").value(car1.getModel()))
                .andExpect(jsonPath("$.mileage").value(car1.getMileage()))
                .andExpect(jsonPath("$.year").value(car1.getYear()))
                .andExpect(jsonPath("$.color").value(car1.getColor()))
                .andExpect(jsonPath("$.createdAt").value(car1.getCreatedAt()))
                .andExpect(jsonPath("$.updatedAt").value(car1.getUpdatedAt()));
    }

    @Test
    void getCar_carWithVinIsNotAvailable_resourceNotFoundExceptionIsThrown() throws Exception {
        when(carService.getCar(car1.getVin())).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/".concat(car1.getVin())).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCar_validValues_carIsCreated() throws Exception {
        when(carService.addCar(any())).thenReturn(car1);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/cars").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car1)))
                .andExpect(status().isCreated());

        verify(carService, times(1)).addCar(car1);
    }

    @Test
    void createCar_notValidValues_badRequestIsReturned() throws Exception {
        when(carService.addCar(any())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/cars").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car1)))
                .andExpect(status().isBadRequest());

        verify(carService, times(1)).addCar(car1);
    }

    @Test
    void updateCar_validValues_acceptedIsReturned() throws Exception {
        when(carService.updateCar(car1.getVin(), car1)).thenReturn(car1);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/cars/".concat(car1.getVin())).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car1)))
                .andExpect(status().isAccepted());

        verify(carService, times(1)).updateCar(car1.getVin(), car1);
    }

    @Test
    void updateCar_notValidValues_badRequestIsReturned() throws Exception {
        when(carService.updateCar(car1.getVin(), car2)).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/cars/".concat(car1.getVin())).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car2)))
                .andExpect(status().isBadRequest());

        verify(carService, times(1)).updateCar(car1.getVin(), car2);
    }

    @Test
    void deleteCar_validValues_OKIsReturned() throws Exception {
        when(carService.deleteCar(car1.getVin())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/".concat(car1.getVin())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService, times(1)).deleteCar(car1.getVin());
    }

    @Test
    void deleteCar_notValidValues_badRequestIsReturned() throws Exception {
        when(carService.deleteCar(any())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/".concat(car1.getVin())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(carService, times(1)).deleteCar(car1.getVin());
    }
}
