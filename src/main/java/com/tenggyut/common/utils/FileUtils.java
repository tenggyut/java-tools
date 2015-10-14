package com.tenggyut.common.utils;


import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.tenggyut.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

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
}
