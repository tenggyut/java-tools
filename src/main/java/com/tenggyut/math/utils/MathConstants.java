package com.tenggyut.math.utils;

import com.google.common.collect.ImmutableMap;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.common.utils.StringsUtils;
import com.tenggyut.regex.RegexUtils;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Map;

/**
 * constants in math
 * <p/>
 * Created by tenggyt on 2016/2/3.
 */
public class MathConstants {
    private static final Logger LOG = LogFactory.getLogger(MathConstants.class);

    public static final Map<String, BigDecimal> CONSTANT_SYMBOLS = ImmutableMap.of(
            "pi", BigDecimal.valueOf(Math.PI)
    );

    public static boolean isConstantSymbol(String symbol) {
        return CONSTANT_SYMBOLS.keySet().contains(symbol);
    }

    public static boolean hasConstantSymbol(String expr) {
        return RegexUtils
                .hasPattern("(" + StringsUtils.join(CONSTANT_SYMBOLS.keySet(), ")|(") + ")", expr);
    }

}
