package com.algodefu.zlogger.util;

import java.io.File;
import java.io.IOException;

/**
 * @author oleg.zherebkin
 */
public final class FileCommon {
    static public int emptyFolder(File folder, boolean ignoreCannotDel) throws IOException {
        int counter = 0;
        if (folder.exists() && folder.isDirectory()) {
            File[] child = folder.listFiles();
            for (int i = 0; i < child.length; i++) {
                File file = child[i];
                if (file.isDirectory()) counter += emptyFolder(file, ignoreCannotDel);
                boolean result = file.delete();
                if (!result && !ignoreCannotDel) {
                    if(!ignoreCannotDel) {
                        throw new IOException("Cannot delete " + file.getAbsolutePath());
                    } else {
                        file.deleteOnExit() ;
                    }
                } else {
                    counter++;
                }
            }
        }
        return counter;
    }

    static public int remove(File file, boolean ignoreCannotDelete) throws Exception {
        int counter = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                counter += emptyFolder(file, ignoreCannotDelete);
            }
            boolean result = file.delete();
            if (!result && !ignoreCannotDelete) {
                throw new Exception("Cannot delete " + file.getAbsolutePath());
            } else {
                counter++;
            }
        }
        return counter;
    }

    static public int removePath (String path) throws Exception {
        int counter = 0;
        File file = new File(path);
        if (file.exists())
            counter = remove(file, false);
        return counter;
    }
}
