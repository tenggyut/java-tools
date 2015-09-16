package com.tenggyut.common.utils;

import com.google.common.base.*;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * string collection utility class
 * <p/>
 * Created by tenggyt on 2015/7/11.
 */
public class StringCollectionUtils {

    /**
     * join a collection of string with the given separator. null or empty string will be skipped.
     *
     * @param elements  elements collection
     * @param separator separator used to join the string collection
     * @return join result
     */
    public static String join(Collection<String> elements, String separator) {
        return Joiner.on(separator).skipNulls().join(Collections2.filter(elements, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return StringUtils.isNotEmpty(input);
            }
        }));
    }

    /**
     * join a collection of object's toString result with the given separator. null object will be skipped.
     *
     * @param elements  elements collection
     * @param separator separator used to join the object collection
     * @return join result
     */
    public static String joinObjects(Collection<?> elements, String separator) {
        return join(Collections2.transform(elements, new Function<Object, String>() {
            @Override
            public String apply(Object input) {
                return input == null ? "" : input.toString();
            }
        }), separator);
    }

    /**
     * use the separator to split the given string into a string list.
     *
     * @param string    target string
     * @param separator target separator
     * @return split result
     */
    public static List<String> split(String string, String separator) {
        return Splitter.on(separator).trimResults().splitToList(string);
    }

    /**
     * use the separator to split the given string into a string list. additionally empty piece will be 
     * omitted
     *
     * @param string    target string
     * @param separator target separator
     * @return split result
     */
    public static List<String> splitAndOmit(String string, String separator) {
        return Splitter.on(separator).omitEmptyStrings().trimResults().splitToList(string);
    }

    /**
     * check whether a string collection is empty or not. null and empty string will not count as a valid element
     *
     * @param strings string collection
     * @return true if collection is empty, false otherwise.
     */
    public static boolean isCollectionEmpty(Collection<String> strings) {
        Optional<Collection<String>> stringsList = Optional.fromNullable(strings);
        return !stringsList.isPresent() || StringUtils.isEmpty(join(strings, ""));
    }
}
