package br.com.azi.patrimoniomobiliario.gateway.integration.halreport.utils;

public class FilenameUtils {

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfLastSeparator(filename);
            return filename.substring(index + 1);
        }
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? "" : filename.substring(index + 1);
        }
    }

    private static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int lastUnixPos = filename.lastIndexOf(47);
            int lastWindowsPos = filename.lastIndexOf(47);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }

    private static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int extensionPos = filename.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(filename);
            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }
}
