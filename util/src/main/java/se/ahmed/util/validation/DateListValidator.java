package se.ahmed.util.validation;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class DateListValidator  {

    public static boolean isValId(List<String> dates){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateValidatorUsingLocalDate validator = new DateValidatorUsingLocalDate(dateTimeFormatter);
        String wrongDateStr = dates.stream()
                .filter(date -> !validator.isValid(date))
                .findFirst()
                .orElse(null);
        if(wrongDateStr == null){
            return true;
        }
        return false;
    }
}
