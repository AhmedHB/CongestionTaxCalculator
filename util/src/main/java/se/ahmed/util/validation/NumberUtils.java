package se.ahmed.util.validation;

import java.util.regex.Pattern;

public class NumberUtils {
    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isParsable(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
