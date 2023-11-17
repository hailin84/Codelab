package org.alive.idealab;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/4/24 14:54
 */
public class DateTimeTest {

    @Test
    public void dateDiff() {
        LocalDate now = LocalDate.now();
        LocalDate past = LocalDate.parse("2023-05-24", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long days = past.until(now, ChronoUnit.DAYS);
        System.out.println(days);
    }

    public boolean isInCutOff(LocalDateTime now ) {
        // LocalDateTime now = LocalDateTime.now();
        if (now.getHour() == 23 || now.getHour() == 0) {
            return true;
        }
        return false;
    }

    @Test
    public void testCutOff() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LocalDate now = LocalDate.now();
        LocalTime t2 = LocalTime.of(0, 59, 59);
        LocalDateTime right = LocalDateTime.of(now, t2);

        System.out.println(right + " - " + isInCutOff(right));
    }
}
