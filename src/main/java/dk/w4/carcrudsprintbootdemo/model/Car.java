package dk.w4.carcrudsprintbootdemo.model;

public record Car(
        String vin,
        String make,
        String model,
        double mileage,
        int year,
        String color,
        long createdAt,
        long updatedAt
) {
    public static Car empty() {
        return new Car("", "", "", 0, 0, "", 0, 0);
    }
    public Car setCreatedAt(long unixTimeStamp) {
        return new Car(vin, make, model, mileage, year, color, unixTimeStamp, unixTimeStamp);
    }

    public Car setUpdatedAt(long unixTimeStamp) {
        return new Car(vin, make, model, mileage, year, color, createdAt, unixTimeStamp);
    }
}
