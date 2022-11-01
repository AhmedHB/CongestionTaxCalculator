package se.ahmed.api.core.tollfeecalculator.vehicle;

public class Emergency implements Vehicle{
    @Override
    public String getVehicleType() {
        return "Emergency";
    }
}
