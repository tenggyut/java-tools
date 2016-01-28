package com.tenggyut.common.utils;

import com.tenggyut.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * utility class for some basic math opts..
 * <p/>
 * Created by tenggyt on 2016/1/28.
 */
public class MathUtils {
    private static final Logger LOG = LogFactory.getLogger(MathUtils.class);

    public static double round(double value, int places) {
        if (places < 0) {
            LOG.warn("try to round {} with a negative digits..use 0 instead.", value);
            places = 0;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
