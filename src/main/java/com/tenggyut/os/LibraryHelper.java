package com.tenggyut.os;

import com.google.common.base.Optional;
import com.google.common.base.StandardSystemProperty;
import com.google.common.io.Files;
import com.tenggyut.common.logging.LogFactory;
import com.tenggyut.config.ResourceFinder;
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

    private static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir")
            + StandardSystemProperty.PATH_SEPARATOR.value() + "tmpdll";

    /**
     * load a dynamic link library shipped within a Jar file.
     * Please note, "dll/library.so" is not supported yet.. Only "library.so"/"library.dll" are tested.
     *
     * @param library the full name(means including extension) of the library.
     * @throws IOException if the loading processes encounters some problem.
     */
    public static void loadLibraryFromJar(String library) throws IOException {
        try {
            cleanup(library);
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

    private static void cleanup(String library) {
        File tmpDir = new File(TEMP_DIR_PATH);
        if (tmpDir.isDirectory()) {
            File[] dllCandidats = tmpDir.listFiles();
            if (dllCandidats != null) {
                for (File dllCandidate : dllCandidats) {
                    if (dllCandidate.getName().contains(library)) {
                        try {
                            dllCandidate.delete();
                        } catch (Exception e) {
                            LOG.debug("try to delete {}..but failed due to {}..ignore it", dllCandidate.getPath(), e);
                        }
                    }
                }
            }
        }
    }

    private static Optional<File> createTempLibrary(String library) {
        File tmpDir = new File(TEMP_DIR_PATH);

        if (!(!tmpDir.exists() || !tmpDir.isDirectory()) || tmpDir.mkdir()) {
            try {
                return Optional.of(File.createTempFile(library, "", tmpDir));
            } catch (IOException e) {
                LOG.error("failed to create tmp lib file {} due to {}", library, e);
                return Optional.absent();
            }
        } else {
            LOG.fatal("failed to create a tmpDir {} to store dll files", tmpDir.getPath());
            return Optional.absent();
        }
    }
}
