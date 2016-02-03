package com.tenggyut.math.utils;

import com.google.common.base.Preconditions;
import com.tenggyut.common.logging.LogFactory;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
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

    public static boolean isArithmeticOpt(char opt) {
        return opt == '+' || opt == '-' || opt == '*' || opt == '/';
    }

    public static boolean verifyBrackets(String expr) {
        Stack<Character> brackets = new Stack<Character>();
        for (char c : expr.toCharArray()) {
            if (isLeftBracket(c)) {
                brackets.push(c);
            } else if (isRightBracket(c) && brackets.isEmpty()) {
                return false;
            } else if (!brackets.isEmpty() && isBracketPair(c, brackets.peek())) {
                brackets.pop();
            }
        }

        return brackets.isEmpty();
    }

    public static boolean isBracketPair(char firstBracketHalf, char secondBracketHalf) {
        char left;
        char right;
        if (isLeftBracket(secondBracketHalf)) {
            left = secondBracketHalf;
            right = firstBracketHalf;
        } else {
            left = firstBracketHalf;
            right = secondBracketHalf;
        }
        return right == ')' && left == '('
                || right == ']' && left == '['
                || right == '}' && left == '{';
    }

    public static boolean isLeftBracket(char bracketHalf) {
        return bracketHalf == '(' || bracketHalf == '[' || bracketHalf == '{';
    }

    public static boolean isRightBracket(char bracketHalf) {
        return bracketHalf == ')' || bracketHalf == ']' || bracketHalf == '}';
    }

    public static boolean isBracket(char bracketHalf) {
        return isLeftBracket(bracketHalf) || isRightBracket(bracketHalf);
    }

    public static int findTheOtherBracket(String expr, int start, char bracketHalf) {
        Stack<Character> stack = new Stack<Character>();
        stack.push(bracketHalf);
        char[] chars = expr.toCharArray();
        if (isLeftBracket(bracketHalf)) {
            for (int i = start; i < chars.length; i++) {
                if (isLeftBracket(chars[i])) {
                    stack.push(chars[i]);
                } else if (isBracketPair(chars[i], stack.peek())) {
                    stack.pop();
                    if (stack.isEmpty()) {
                        return i;
                    }
                }
            }
        } else if (isRightBracket(bracketHalf)) {
            for (int i = start; i >= 0; i--) {
                if (isRightBracket(chars[i])) {
                    stack.push(chars[i]);
                } else if (isBracketPair(chars[i], stack.peek())) {
                    stack.pop();
                    if (stack.isEmpty()) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static String replaceConstantSymbolWithValue(String expr) {
        if (!MathConstants.hasConstantSymbol(expr)) return expr;
        String result = expr;
        for (Map.Entry<String, BigDecimal> entry : MathConstants.CONSTANT_SYMBOLS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue().toPlainString());
        }
        return result;
    }
}
