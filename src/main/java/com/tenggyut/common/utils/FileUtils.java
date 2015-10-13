package com.tenggyut.common.utils;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.config.ResourceFinder;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * File utility
 *
 * Created by tenggyt on 2015/9/16.
 */
public class FileUtils {
    private static final Logger LOG = LogFactory.getLogger(FileUtils.class);
    public static final String DEFAULT_LINE_SEPARATOR = "\n";

    /**
     * write content to file with given path. if the given path exists, then
     *
     * @param content  file content
     * @param filePath file path
     * @throws IOException
     */
    public static void write(byte[] content, String filePath) throws IOException {
        File out = new File(filePath);
        if (out.exists()) {
            if (!out.delete()) {
                LOG.warn("file {} exists, do nothing.", filePath);
                return;
            }
        }
        if (out.createNewFile()) {
            write(content, out);
        }
    }

    /**
     * write content to file with given path. please note, the given file should exist when invoking this
     * method
     *
     * @param content content
     * @param out target file
     * @throws IOException
     */
    public static void write(byte[] content, File out) throws IOException {
        Preconditions.checkArgument(out.exists(), "file " + out.getName() + "does not exist");
        Files.write(content, out);
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
            in = ResourceFinder.findResources(resource);
        } catch (IOException e) {
            LOG.error("failed to find {}", resource);
            return Lists.newArrayListWithCapacity(0);
        }
        InputStreamReader inr = new InputStreamReader(in);
        try {
            return StringsUtils.split(CharStreams.toString(inr), DEFAULT_LINE_SEPARATOR);
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
