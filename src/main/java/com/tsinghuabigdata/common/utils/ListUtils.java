package com.tsinghuabigdata.common.utils;

import com.google.common.collect.Sets;
import com.tsinghuabigdata.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * list utility class
 * Created by tenggyt on 2015/7/30.
 */
public class ListUtils {
    private static final Logger LOG = LogFactory.getLogger(ListUtils.class);

    public static <T> boolean isTheSame(List<T> listOne, List<T> listTwo) {
        Set<T> intersection = Sets.intersection(Sets.newHashSet(listOne), Sets.newHashSet(listTwo));
        return listOne.size() == intersection.size() && listTwo.size() == intersection.size();
    }
}
