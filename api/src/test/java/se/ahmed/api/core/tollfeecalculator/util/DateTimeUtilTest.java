package se.ahmed.api.core.tollfeecalculator.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import se.ahmed.api.core.tollfee.TollFeeTime;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
public class DateTimeUtilTest {

    @Test
    public void isWithinTimeWindowOk(){
        String candidate = "12:00";
        String start = "10:00";
        String end = "18:00";
        TollFeeTime tollFeeTime = new TollFeeTime(start, end, 10);
        LocalTime candidateTime = LocalTime.parse(candidate);

        boolean result = DateTimeUtil.isWithinTimeWindow(candidateTime, tollFeeTime);
        Assert.isTrue(result, "Is between");
    }

    @Test
    public void isWithinTimeWindowNok(){
        String candidate = "19:00";
        String start = "10:00";
        String end = "18:00";
        TollFeeTime tollFeeTime = new TollFeeTime(start, end, 10);
        LocalTime candidateTime = LocalTime.parse(candidate);

        boolean result = DateTimeUtil.isWithinTimeWindow(candidateTime, tollFeeTime);

        Assert.isTrue(result==false, "It not between" );
    }
}
