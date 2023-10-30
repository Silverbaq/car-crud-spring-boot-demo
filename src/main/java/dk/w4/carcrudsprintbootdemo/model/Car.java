package dk.w4.carcrudsprintbootdemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car{

        @Id
        String vin;
        String make;
        String model;
        double mileage;
        int year;
        String color;
        long createdAt;
        long updatedAt;

        /*
    public static Car empty() {
        return new Car("", "", "", 0, 0, "", 0, 0);
    }
    public Car setCreatedAt(long unixTimeStamp) {
        return new Car(vin, make, model, mileage, year, color, unixTimeStamp, unixTimeStamp);
    }

    public Car setUpdatedAt(long unixTimeStamp) {
        return new Car(vin, make, model, mileage, year, color, createdAt, unixTimeStamp);
    }
         */
}
