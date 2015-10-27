package com.tenggyut.config;

import com.tenggyut.common.logging.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * config
 * <p/>
 * Created by tenggyt on 2015/10/27.
 */
public class Configuration {
    private static final Logger LOG = LogFactory.getLogger(Configuration.class);

    private Properties prop;
    private String fileName;

    private Configuration(String fileName) {
        this.fileName = fileName;
        this.prop = ResourceFinder.buildProperties(fileName);
    }

    public boolean merge(String anotherFileName) {
        InputStream is = null;
        try {
            is = ResourceFinder.findResources(anotherFileName);
            prop.load(is);
            return true;
        } catch (IOException e) {
            LOG.error("failed to merge another properties file {}, because {}", anotherFileName, e.getMessage());
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("failed to close InputStream for file {}, because {}", anotherFileName, e.getMessage());
                }
            }
        }
    }

    public String getProperty(String key) {
        return prop.getProperty(key);
    }

    public int getProperty(String key, int defaultValue) {
        String valueCandidate = getProperty(key);
        try {
            return Integer.parseInt(valueCandidate);
        } catch (Exception e) {
            LOG.warn("try to get int property from key {} in {}. but failed to convert {} to int. so return default {}"
                    , key, fileName, valueCandidate, defaultValue);
            return defaultValue;
        }
    }

    public String getProperty(String key, String defaultValue) {
        String valueCandidate = getProperty(key);
        return StringUtils.isEmpty(valueCandidate) ? defaultValue : valueCandidate;
    }

    public static Configuration build(String propertiesFilename) {
        return new Configuration(propertiesFilename);
    }
}
