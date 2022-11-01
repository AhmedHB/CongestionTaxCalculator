package se.ahmed.api.core.tollfeecalculator.calculation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import se.ahmed.api.core.tollfee.TaxRules;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeTime;
import se.ahmed.api.core.tollfeecalculator.vehicle.Car;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class CongestionTaxCalculatorTest {
    private TollFee testTollFeeData;

    private TollFee getTollFeeGothenborg(){
        TollFee api = TollFee.builder()
                .city("göteborg")
                .tollFeeTimes(new ArrayList<TollFeeTime>(){{
                    add(new TollFeeTime("06:00", "06:29", 8));
                    add(new TollFeeTime("06:30", "06:59", 13));
                    add(new TollFeeTime("07:00", "07:59", 18));
                    add(new TollFeeTime("08:00", "08:29", 13));
                    add(new TollFeeTime("08:30", "14:59", 8));
                    add(new TollFeeTime("15:00", "15:29", 13));
                    add(new TollFeeTime("15:30", "16:59", 18));
                    add(new TollFeeTime("17:00", "17:59", 13));
                    add(new TollFeeTime("18:00", "18:29", 8));
                }})
                .taxRules(TaxRules.builder().tollFreeVehicle(new ArrayList<String>(){{
                    add("Motorcycle");
                    add("Tractor");
                    add("Emergency");
                    add("Diplomat");
                    add("Foreign");
                    add("Military");
                }}).useTollFreeDay(true).build())
                .build();

        return api;
    }

    @Before
    public void setup(){
        testTollFeeData = getTollFeeGothenborg();
    }

    @Test
    public void GetTollFeeSixAclockResult8(){
        String candidateDate = "2022-06-13 06:00:00";
        Car car = new Car();


        int value = CongestionTaxCalculator.GetTollFee(candidateDate, car, testTollFeeData);
        Assert.isTrue(value == 8, "Fee is correct");
    }

    @Test
    public void GetTollFeeNinteenAclockResult0(){
        String candidateDate = "2022-06-13 19:00:00";
        Car car = new Car();

        int value = CongestionTaxCalculator.GetTollFee(candidateDate, car, testTollFeeData);
        Assert.isTrue(value == 0, "Fee is correct");
    }

    @Test
    public void GetTollFeeIsWeekendResult0(){
        String candidateDate = "2022-06-12 18:00:00";
        Car car = new Car();

        int value = CongestionTaxCalculator.GetTollFee(candidateDate, car, testTollFeeData);
        System.out.println(value);
        Assert.isTrue(value == 0, "Fee is correct");
    }

    @Test
    public void GetTollFeeIsRedDayResult0(){
        String candidateDate = "2022-06-06 18:00:00";
        Car car = new Car();

        int value = CongestionTaxCalculator.GetTollFee(candidateDate, car, testTollFeeData);
        Assert.isTrue(value == 0, "Fee is correct");
    }

    @Test
    public void GetTollFeeTaxTwoDatesWithinOneHourResult18(){
        Car car = new Car();

        List<String> dates = new ArrayList<>();
        dates.add("2013-01-14 07:30:00");
        dates.add("2013-01-14 08:00:00");

        int value = CongestionTaxCalculator.GetTollFeeTax(dates, car, testTollFeeData);
        Assert.isTrue(value == 18, "Fee is correct");
    }

    @Test
    public void GetTollFeeTaxTwoDatesWithinTwoHourResult26(){
        Car car = new Car();

        List<String> dates = new ArrayList<>();

        dates.add("2013-01-14 08:00:00");
        dates.add("2013-01-14 09:00:00");
        dates.add("2013-01-14 07:30:00");

        int value = CongestionTaxCalculator.GetTollFeeTax(dates, car, testTollFeeData);
        Assert.isTrue(value == 26, "Fee is correct");
    }

}
