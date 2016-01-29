package com.tenggyut.common.utils;

import com.google.common.base.Preconditions;
import com.tenggyut.common.logging.LogFactory;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

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

    public static double round(String value, int places) throws IllegalArgumentException {
        if (places < 0) {
            LOG.warn("try to round {} with a negative digits..use 0 instead.", value);
            places = 0;
        }

        Preconditions.checkArgument(NumberUtils.isNumber(value), value + "is not a number");

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean verifyBrackets(String expr) {
        Stack<Character> brackets = new Stack<Character>();

        for (char c : expr.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                brackets.push(c);
            } else if (c == ')' && brackets.peek() == '('
                    || c == ']' && brackets.peek() == '['
                    || c == '}' && brackets.peek() == '{') {
                brackets.pop();
            }
        }

        return brackets.isEmpty();
    }
}
