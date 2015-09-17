package com.tenggyut.common.logging;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * logger factory
 * <p/>
 * Created by tenggyt on 2015/7/22.
 */
public class LogFactory {
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    public static Logger getLogger(String loggerName) {
        return LogManager.getLogger(loggerName);
    }

}
