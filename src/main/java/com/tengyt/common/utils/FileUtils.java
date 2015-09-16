package com.tengyt.common.utils;


import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.tengyt.common.logging.LogFactory;
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

    public static void write(byte[] content, String filePath) throws IOException {
        File out = new File(filePath);
        if (out.exists()) {
            out.delete();
        }
        write(content, out);
    }

    public static void write(byte[] content, File out) throws IOException {
        Preconditions.checkArgument(out.exists(), "file " + out.getName() + "does not exist");
        Files.write(content, out);
    }
}
