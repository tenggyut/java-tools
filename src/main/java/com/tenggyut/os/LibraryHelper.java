package com.tenggyut.os;

import com.google.common.base.StandardSystemProperty;
import com.google.common.io.Files;
import com.tenggyut.config.ResourceFinder;

import java.io.File;
import java.io.IOException;

/**
 * this helper class is designed to assist developers loading dynamic link library
 * shipped within a Jar
 * <p/>
 * Created by tenggyt on 2016/1/12.
 */
public class LibraryHelper {

    /**
     * load a dynamic link library shipped within a Jar file.
     * Please note, "dll/library.so" is not supported yet.. Only "library.so"/"library.dll" are tested.
     *
     * @param library the full name(means including extension) of the library.
     * @throws IOException if the loading processes encounters some problem.
     */
    public static void loadLibraryFromJar(String library) throws IOException {
        try {
            File dll = File.createTempFile(library, getSuffixByOS());
            Files.write(ResourceFinder.readResourcesAsBytes(library), dll);

            dll.deleteOnExit();

            System.load(dll.toString());
        } catch (IOException e) {
            throw new IOException("failed to copy " + library
                    + " out of jar and load it due to " + e);
        }
    }

    private static String getSuffixByOS() {
        String os = StandardSystemProperty.OS_NAME.value();
        if (os == null || os.contains("windows")) {
            return "dll";
        } else if (os.contains("linux")) {
            return "so";
        } else {
            return null;
        }
    }
}
