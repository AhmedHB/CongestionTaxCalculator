package se.ahmed.api.core.tollfeecalculator.calculation;

import se.ahmed.api.core.tollfee.TaxRules;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeTime;
import se.ahmed.api.core.tollfeecalculator.util.DateTimeUtil;
import se.ahmed.api.core.tollfeecalculator.util.TollFreeDay;
import se.ahmed.api.core.tollfeecalculator.util.TollFreeVehicleUtil;
import se.ahmed.api.core.tollfeecalculator.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CongestionTaxCalculator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static int GetTollFee(String date, Vehicle vehicle, TollFee tollfee){
        LocalTime candidateTime = LocalTime.parse(date, formatter);
        LocalDateTime candidateDate = LocalDateTime.parse(date, formatter);

        TaxRules taxRules = tollfee.getTaxRules();
        if(taxRules.isUseTollFreeDay()){
            if(TollFreeDay.IsTollFreeDate(candidateDate)){
                return 0;
            }
        }

        if(TollFreeVehicleUtil.IsTollFreeVehicle(vehicle, taxRules.getTollFreeVehicle()) ){
            return 0;
        }

        List<TollFeeTime> feeTimes = tollfee.getTollFeeTimes();
        Integer fee = feeTimes.stream()
                .filter(tft -> DateTimeUtil.isWithinTimeWindow(candidateTime, tft))
                .map(tft -> tft.getFee())
                .findFirst()
                .orElse(0);

        return fee.intValue();
    }

    public static int GetTollFeeTax(List<String> dates, Vehicle vehicle, TollFee tollfee){

        int totalFee = 0;
        List<String> sortedDateList = dates.stream()
                .sorted(Comparator.comparing(date-> DateTimeUtil.getLocalDateTime(date)))
                .collect(Collectors.toList());

        String intervalStart = sortedDateList.get(0);
        LocalDateTime intervalStartDate = LocalDateTime.parse(intervalStart, formatter);

        for(String date :sortedDateList){
            int nextFee = GetTollFee(date, vehicle, tollfee);
            int tempFee = GetTollFee(intervalStart, vehicle, tollfee);

            LocalDateTime currdate = LocalDateTime.parse(date, formatter);

            long time[]  = DateTimeUtil.getTime(intervalStartDate, currdate);
            long minutes = time[0] *60 + time[1];

            if (minutes <= 60)
            {
                if (totalFee > 0)
                    totalFee -= tempFee;

                if (nextFee >= tempFee)
                    tempFee = nextFee;

                totalFee += tempFee;
            }else
            {
                totalFee += nextFee;
            }

        }

        if (totalFee > 60)
            totalFee = 60;

        return totalFee;
    }


}
