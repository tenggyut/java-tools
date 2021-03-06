package com.tenggyut.common.utils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tenggyut.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * list utility class
 * Created by tenggyt on 2015/7/30.
 */
public class ListUtils {
    private static final Logger LOG = LogFactory.getLogger(ListUtils.class);

    public static <T> boolean isTheSame(List<T> listOne, List<T> listTwo) {
        if (listOne.size() != listTwo.size()) return false;

        Set<T> setOne = Sets.newHashSet(listOne);
        Set<T> setTwo = Sets.newHashSet(listTwo);
        Set<T> intersection = Sets.intersection(setOne, setTwo);
        return setOne.size() == intersection.size() && setTwo.size() == intersection.size();
    }

    public static <T> List<T> intersection(Collection<T> listOne, Collection<T> listTwo) {
        return Lists.newArrayList(Sets.intersection(Sets.newHashSet(listOne), Sets.newHashSet(listTwo)));
    }

    public static <T, S> List<S> flatmap(Collection<T> list, Function<T, List<S>> function) {
        return Lists.newArrayList(FluentIterable.from(list).transformAndConcat(function));
    }

    public static <T, S> List<S> map(Collection<T> list, Function<T, S> function) {
        return Lists.newArrayList(Collections2.transform(list, function));
    }

    public static <T> List<T> filter(Collection<T> list, Predicate<T> predicate) {
        return Lists.newArrayList(Collections2.filter(list, predicate));
    }

    public static <T, S> List<S> flatmap(Iterable<T> list, Function<T, List<S>> function) {
        return Lists.newArrayList(FluentIterable.from(list).transformAndConcat(function));
    }

    public static <T, S> List<S> map(Iterable<T> list, Function<T, S> function) {
        return Lists.newArrayList(FluentIterable.from(list).transform(function));
    }

    public static <T> List<T> filter(Iterable<T> list, Predicate<T> predicate) {
        return Lists.newArrayList(FluentIterable.from(list).filter(predicate));
    }

    public static <T, S> List<S> flatmap(T[] list, Function<T, List<S>> function) {
        return Lists.newArrayList(FluentIterable.of(list).transformAndConcat(function));
    }

    public static <T, S> List<S> map(T[] list, Function<T, S> function) {
        return Lists.newArrayList(FluentIterable.of(list).transform(function));
    }

    public static <T> List<T> filter(T[] list, Predicate<T> predicate) {
        return Lists.newArrayList(FluentIterable.of(list).filter(predicate));
    }

    /**
     * check if all the elements satisfy Predicate.apply. if not return false, otherwise true
     *
     * @param list      unchecked list
     * @param predicate match logic.
     * @return if all elements satisfy the given predicate, then return true, otherwise false
     */
    public static <T> boolean isAllMatch(Iterable<T> list, Predicate<T> predicate) {
        for (T t : list) {
            if (!predicate.apply(t)) {
                return false;
            }
        }

        return true;
    }
}
