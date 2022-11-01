package se.ahmed.api.core.tollfeecalculator.util;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TollFreeDay {

    public static boolean IsTollFreeDate(final LocalDateTime candidate){
        if(isMonthOfJuly(candidate)){
            return true;
        }

        if(isWeekend(candidate)){
            return true;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDateTime = candidate.format(formatter);

        int year = candidate.getYear();
        List<String> redDays = calculateRedDaysForCalendarYear(year);
        String matchedDate = redDays.stream()
                .filter(redday -> redday.equalsIgnoreCase(formatDateTime))
                .findFirst()
                .orElse("");

        if(!StringUtils.isEmpty(matchedDate)){
            return true;
        }
        return false;
    }

    public static boolean isWeekend(final LocalDateTime ld)
    {
        DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }

    public static boolean isMonthOfJuly(final LocalDateTime ld){
        Month month = ld.getMonth();
        return month == Month.JULY;
    }

    public static List<String> calculateRedDaysForCalendarYear(int year) {
        List<String> calcRedDays = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.getTime();

        Calendar tempcal = Calendar.getInstance();
        tempcal.getTime();
        tempcal.set(Calendar.YEAR, year);
        tempcal.set(Calendar.MONTH, 0);
        tempcal.set(Calendar.DAY_OF_MONTH, 0);

        Calendar endCal = Calendar.getInstance();
        endCal.getTime();
        endCal.add(Calendar.YEAR, 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(cal.getTime());

        //New years day
        tempcal.set(cal.get(Calendar.YEAR), 0, 1);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        //Trettondedag jul
        tempcal.set(cal.get(Calendar.YEAR), 0, 6);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        tempcal.set(cal.get(Calendar.YEAR), 0, 1);
        //Easter day
        for (int oneyr = 1; oneyr < 2; oneyr++) {
            int y = tempcal.get(Calendar.YEAR);
            int a = y % 19;
            int b = y / 100;
            int c = y % 100;
            int d = b / 4;
            int e = b % 4;
            int f = (b + 8) / 25;
            int g = (b - f + 1) / 3;
            int h = (19 * a + b - d - g + 15) % 30;
            int i = c / 4;
            int k = c % 4;
            int l = (32 + 2 * e + 2 * i - h - k) % 7;
            int m = (a + 11 * h + 22 * l) / 451;
            int month = (h + l - 7 * m + 114) / 31;
            int day = ((h + l - 7 * m + 114) % 31) + 1;
            tempcal.set(tempcal.get(Calendar.YEAR), month - 1, day);
            if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today))) {
                tempcal.add(Calendar.YEAR, 1);
                oneyr = 0;
            }
        }
        calcRedDays.add(df.format(tempcal.getTime()));

        //L?ngfredagen
        tempcal.add(Calendar.DATE, -2);
        calcRedDays.add(df.format(tempcal.getTime()));

        //Second day of easter
        tempcal.add(Calendar.DATE, 3);
        calcRedDays.add(df.format(tempcal.getTime()));

        //Kristi himmelsf?rdsdag
        tempcal.add(Calendar.DATE, 38);
        calcRedDays.add(df.format(tempcal.getTime()));

        //Pingstdagen
        tempcal.add(Calendar.DATE, 10);
        calcRedDays.add(df.format(tempcal.getTime()));

        //First of May
        tempcal.set(cal.get(Calendar.YEAR), 4, 1);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        //Sweden's National day
        tempcal.set(cal.get(Calendar.YEAR), 5, 6);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        //Midsummer day
        tempcal.set(cal.get(Calendar.YEAR), 0, 1);
        for (int j = 0; j < 7; j++) {
            tempcal.set(tempcal.get(Calendar.YEAR), 5, 20 + j);
            if ((tempcal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && (tempcal.getTimeInMillis() > cal.getTimeInMillis() || df.format(tempcal.getTime()).equals(today))) {
                break;
            } else if (j == 6) {
                tempcal.add(Calendar.YEAR, 1);
                j = 0;
            }
        }
        calcRedDays.add(df.format(tempcal.getTime()));

        //Midsommarafton
        tempcal.add(Calendar.DATE, -1);
        calcRedDays.add(df.format(tempcal.getTime()));

        //All hallows eve
        tempcal.set(cal.get(Calendar.YEAR), 0, 1);
        for (int n = 0; n < 7; n++) {
            if (n == 0)
                tempcal.set(tempcal.get(Calendar.YEAR), 9, 31);
            else
                tempcal.set(tempcal.get(Calendar.YEAR), 10, 0 + n);
            if ((tempcal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && (tempcal.getTimeInMillis() > cal.getTimeInMillis() || df.format(tempcal.getTime()).equals(today))) {
                break;
            } else if (n == 6) {
                tempcal.add(Calendar.YEAR, 1);
                n = -1;
            }
        }
        calcRedDays.add(df.format(tempcal.getTime()));
        //Christmas eve
        tempcal.set(cal.get(Calendar.YEAR), 11, 24);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        //Christmas day
        tempcal.set(cal.get(Calendar.YEAR), 11, 25);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        //2nd day of Christmas
        tempcal.set(cal.get(Calendar.YEAR), 11, 26);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        //New years eve
        //tempcal.set(cal.get(Calendar.YEAR), 11, 31);
        tempcal.set( year , 11, 31);
        if (tempcal.getTimeInMillis() < cal.getTimeInMillis() && (!df.format(tempcal.getTime()).equals(today)))
            tempcal.add(Calendar.YEAR, 1);
        calcRedDays.add(df.format(tempcal.getTime()));

        return calcRedDays;
    }
}
