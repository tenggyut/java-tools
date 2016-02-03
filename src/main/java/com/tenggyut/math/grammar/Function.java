package com.tenggyut.math.grammar;

import com.google.common.collect.Lists;
import com.tenggyut.common.logging.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * function enum
 * <p/>
 * Created by tenggyt on 2015/7/16.
 */
public enum Function {
    PLUS("+"),
    MINUS("-"),
    PLUSMINUS("±"),
    MULTIPLY("*"),
    DIVIDE("/"),
    POWER("^"),
    ABS("abs"),
    SIN("sin"),
    COS("cos"),
    TAN("tan"),
    EQUAL("="),
    INEQUAL("<"),
    INEQUAL_BIGGER(">"),
    NOTEQUAL("<>"),
    EQUAL_OR_INEQUAL("<="),
    EQUAL_OR_INEQUAL_BIGGER(">="),
    NAME("symbol"),
    NUMBER("number");

    private static final Logger LOG = LogFactory.getLogger(Function.class);

    String literal;

    Function(String opt) {
        this.literal = opt;
    }

    public static Function parse(String opt) {
        if (StringUtils.isEmpty(opt)) return null;
        if (opt.equalsIgnoreCase("+")) {
            return PLUS;
        } else if (opt.equalsIgnoreCase("-")) {
            return MINUS;
        } else if (opt.equals("±")) {
            return PLUSMINUS;
        } else if (opt.equalsIgnoreCase("*")) {
            return MULTIPLY;
        } else if (opt.equalsIgnoreCase("/") || opt.equalsIgnoreCase("Fraction")) {
            return DIVIDE;
        } else if (opt.equalsIgnoreCase("^") || opt.equalsIgnoreCase("**")) {
            return POWER;
        } else if (opt.equalsIgnoreCase("abs")) {
            return ABS;
        } else if (opt.equals("sin")) {
            return SIN;
        } else if (opt.equals("tan")) {
            return TAN;
        } else if (opt.equals("cos")) {
            return COS;
        } else if (opt.equalsIgnoreCase("<")) {
            return INEQUAL;
        } else if (opt.equalsIgnoreCase(">")) {
            return INEQUAL_BIGGER;
        } else if (opt.equalsIgnoreCase("=")) {
            return EQUAL;
        } else if (opt.equalsIgnoreCase("<>")) {
            return NOTEQUAL;
        } else if (opt.equalsIgnoreCase("<=")) {
            return EQUAL_OR_INEQUAL;
        } else if (opt.equalsIgnoreCase(">=")) {
            return EQUAL_OR_INEQUAL_BIGGER;
        } else if (opt.equalsIgnoreCase("symbol")) {
            return NAME;
        } else if (opt.equalsIgnoreCase("number") || opt.equalsIgnoreCase("Integer") || opt.equalsIgnoreCase("float")) {
            return NUMBER;
        } else {
            LOG.error("invalid function string: {}", opt);
            return null;
        }
    }

    public static List<String> getFuncs() {
        return Lists.newArrayList(
                Function.POWER.toString()
                , Function.ABS.toString()
                , Function.SIN.toString()
                , Function.COS.toString()
                , Function.TAN.toString()
        );
    }

    @Override
    public String toString() {
        return literal;
    }

}
