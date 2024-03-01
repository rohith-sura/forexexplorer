package com.forexexplorer.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@UtilityClass
public final class Utility {

    public static BigDecimal doubleToBigDecimal(Double value) {
        return BigDecimal.valueOf(value);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
       return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withNano(0);
    }

    public static BigDecimal findPercentage(BigDecimal min, BigDecimal max) {
        BigDecimal percentageChange = null;
        if(max.compareTo(min) > 0) {
            BigDecimal difference = max.subtract(min);
            percentageChange = (difference.divide(max, 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100));
        } else if (max.compareTo(min) < 0 ) {
            BigDecimal difference = min.subtract(max);
            percentageChange =  (difference.divide(min, 4, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100)).negate();
        }
        return percentageChange;
    }
}
