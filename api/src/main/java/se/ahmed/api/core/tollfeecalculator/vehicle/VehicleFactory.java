package se.ahmed.api.core.tollfeecalculator.vehicle;

public class VehicleFactory {

    public static Vehicle getVehile(String vehicle){
        if(vehicle == null){
            return null;
        }

        if(vehicle.equalsIgnoreCase("Car")) {
            return new Car();
        }else if(vehicle.equalsIgnoreCase("Diplomat")){
            return new Diplomat();
        }else if(vehicle.equalsIgnoreCase("Emergency")){
            return new Emergency();
        }else if(vehicle.equalsIgnoreCase("Foreign")){
            return new Foreign();
        }else if(vehicle.equalsIgnoreCase("Military")){
            return new Military();
        }else if(vehicle.equalsIgnoreCase("Motorcycle")){
            return new Motorcycle();
        }else if(vehicle.equalsIgnoreCase("Tractor")){
            return new Tractor();
        }

        return null;
    }
}
