package se.ahmed.api.core.tollfeecalculator.util;

import se.ahmed.api.core.tollfee.TollFeeTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateTimeUtil {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    public static boolean isWithinTimeWindow(LocalTime candidate, TollFeeTime tollFeeTime) {
        String start = tollFeeTime.getStartTime();
        String end = tollFeeTime.getEndTime();

        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);

        return isBetween(candidate, startTime, endTime);
    }

    private static boolean isBetween(LocalTime candidate, LocalTime start, LocalTime end) {
        return !candidate.isBefore(start) && !candidate.isAfter(end);  // Inclusive.
    }


    public static long[] getTime(LocalDateTime dob, LocalDateTime now) {
        /*LocalDateTime today = LocalDateTime.of(now.getYear(),
                now.getMonthValue(), now.getDayOfMonth(), dob.getHour(), dob.getMinute(), dob.getSecond());*/
        Duration duration = Duration.between(dob, now);

        long seconds = duration.getSeconds();

        long hours = seconds / SECONDS_PER_HOUR;
        long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
        long secs = (seconds % SECONDS_PER_MINUTE);

        return new long[]{hours, minutes, secs};
    }

    public static LocalDateTime getLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localdatetime = LocalDateTime.parse(date, formatter);
        return localdatetime;
    }




}
