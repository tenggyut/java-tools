package com.tenggyut.config;

import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.common.utils.FileUtils;
import com.tenggyut.common.utils.StringsUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class ResourceFinder {
    private static final Logger LOG = LogFactory.getLogger(ResourceFinder.class);

    /**
     * find the resource by the given fileName. this method will search classpath root(/) first. if nothing found,
     * it will search the entire classpath.
     *
     * @param fileName the required resource path
     * @return resource
     * @throws IOException
     */
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

    /**
     * read resource file
     *
     * @param resource resource file path
     * @return resource file content.
     */
    public static List<String> readResource(String resource) {
        InputStream in;
        try {
            in = findResources(resource);
        } catch (IOException e) {
            LOG.error("failed to find {}", resource);
            return Lists.newArrayListWithCapacity(0);
        }
        InputStreamReader inr = new InputStreamReader(in);
        try {
            return StringsUtils.split(CharStreams.toString(inr), FileUtils.DEFAULT_LINE_SEPARATOR);
        } catch (IOException e) {
            LOG.error("failed to read {} ", resource);
            return Lists.newArrayListWithCapacity(0);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LOG.error("failed to close InputStream because {}", e.getMessage());
            }
        }
    }
}
