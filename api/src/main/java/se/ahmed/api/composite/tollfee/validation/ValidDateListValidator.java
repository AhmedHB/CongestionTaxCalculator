package se.ahmed.api.composite.tollfee.validation;

import jdk.internal.joptsimple.internal.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ValidDateListValidator implements ConstraintValidator<ValidDateList, List<String>> {

    @Override
    public boolean isValid(List<String> valueList, ConstraintValidatorContext constraintValidatorContext) {
        boolean notValidDate = false;
        for(String date : valueList){
            if(!isValidFormat("yyyy-MM-dd HH:mm:ss", date)){
                notValidDate = true;
                break;
            }
        }

        return !notValidDate;
    }

    private static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (value != null){
                date = sdf.parse(value);
                if (!value.equals(sdf.format(date))) {
                    date = null;
                }
            }

        } catch (ParseException ex) {
        }
        return date != null;
    }
}