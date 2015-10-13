package com.tenggyut.config;

import com.google.common.base.StandardSystemProperty;
import com.google.common.io.Resources;
import com.tenggyut.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ResourceFinder {
    private static final Logger LOG = LogFactory.getLogger(ResourceFinder.class);

    public static InputStream findResources(String fileName) throws IOException {
        URL url;
        try {
            url = Resources.getResource(StandardSystemProperty.FILE_SEPARATOR.value() + fileName);
            LOG.warn("find " + fileName + " in the root of classpath. use it to override the build-in settings");
        } catch (IllegalArgumentException e) {
            LOG.warn("can't not find " + fileName + " in the root of classpath. try built-in settings");
            try {
                url = Resources.getResource(fileName);
            } catch (IllegalArgumentException e1) {
                throw new IOException("can't find " + fileName + " in the classpath");
            }
        }
        if (url != null) {
            return url.openStream();
        } else {
            throw new IOException("something is wrong when loading the file " + fileName);
        }
    }
}