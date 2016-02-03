package com.tenggyut.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * common regex and pattern constants
 * <p/>
 * Created by tenggyt on 2015/5/27.
 */
public class RegexUtils {

    public static Matcher getMatcher(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

    public static boolean hasPattern(String regex, String input) {
        Matcher matcher = getMatcher(regex, input);
        return matcher.find();
    }
}
