package com.tenggyut.os;

import com.google.common.base.Optional;
import com.google.common.base.StandardSystemProperty;
import com.google.common.io.Files;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.config.ResourceFinder;
import com.tenggyut.exception.RetryFailedException;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * this helper class is designed to assist developers loading dynamic link library
 * shipped within a Jar
 * <p/>
 * Created by tenggyt on 2016/1/12.
 */
public class LibraryHelper {
    private static final Logger LOG = LogFactory.getLogger(LibraryHelper.class);

    /**
     * load a dynamic link library shipped within a Jar file.
     * Please note, "dll/library.so" is not supported yet.. Only "library.so"/"library.dll" are tested.
     *
     * @param library the full name(means including extension) of the library.
     * @throws IOException if the loading processes encounters some problem.
     */
    public static void loadLibraryFromJar(String library) throws IOException {
        try {
            String libFilename = library + getSuffixByOS();
            Optional<File> dll = createTempLibrary(library);
            if (!dll.isPresent()) {
                throw new IOException("failed to create temp library file");
            } else {
                Files.write(ResourceFinder.readResourcesAsBytes(libFilename), dll.get());

                dll.get().deleteOnExit();

                System.load(dll.get().getPath());
            }
        } catch (IOException e) {
            throw new IOException("failed to copy " + library
                    + " out of jar and load it due to " + e);
        }
    }

    private static String getSuffixByOS() {
        String os = StandardSystemProperty.OS_NAME.value();
        if (os == null || os.toLowerCase().contains("windows")) {
            return ".dll";
        } else if (os.toLowerCase().contains("linux")) {
            return ".so";
        } else {
            return ".tmp";
        }
    }

    private static Optional<File> createTempLibrary(String library) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String libFilename = library + getSuffixByOS();
        final File dll = new File(tmpDir + StandardSystemProperty.FILE_SEPARATOR.value() + libFilename);
        if (dll.exists()) {
            LOG.info("dll {} exists already, use it", dll.getPath());
            return Optional.of(dll);
        }

        try {
            return new RetryWorker<Boolean>("create " + dll.getPath()) {

                @Override
                protected Optional<Boolean> doWork() {
                    try {
                        return Optional.of(dll.createNewFile());
                    } catch (IOException e) {
                        return Optional.of(false);
                    }
                }

            }.doWorkWithRetry() ? Optional.of(dll) : Optional.<File>absent();
        } catch (RetryFailedException e) {
            LOG.error("failed to delete previews lib {} due to {}", dll.getPath(), e);
            return Optional.absent();
        }
    }
}
