package se.ahmed.api.core.tollfeecalculator.util;

import se.ahmed.api.core.tollfeecalculator.vehicle.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TollFreeVehicleUtil {
    public static boolean IsTollFreeVehicle(Vehicle vehicle, List<String> tollFreeVehicle) {
        if (vehicle == null) return false;
        String vehicleType = vehicle.getVehicleType();
        return tollFreeVehicle.stream()
                .anyMatch(x -> x.equalsIgnoreCase(vehicleType));
    }
}
