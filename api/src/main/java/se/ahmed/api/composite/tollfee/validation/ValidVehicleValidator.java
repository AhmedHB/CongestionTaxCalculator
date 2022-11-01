package se.ahmed.api.composite.tollfee.validation;

import jdk.internal.joptsimple.internal.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ahmed.api.core.tollfeecalculator.vehicle.Vehicle;
import se.ahmed.api.core.tollfeecalculator.vehicle.VehicleFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidVehicleValidator implements ConstraintValidator<ValidVehicle, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Vehicle vehicle = VehicleFactory.getVehile(value);
        return vehicle != null?true:false;
    }
}