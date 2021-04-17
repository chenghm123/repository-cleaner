package org.accelerator.maven.repositorycleaner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MainRunner {

    private static final IOFileFilter DELETE_DIRECTORY_FILEFILTER =
            FileFilterUtils.deleteDirectoryFileFilter();

    public static void main(String[] args) {
        FileUtils.listFilesAndDirs(Config.REPOSITORY_PATH_FILE,
                FileFilterUtils.deleteFileFileFilter(),
                FileFilterUtils.trueFileFilter())
                .forEach(MainRunner::doDeleteFile);
    }

    private static void doDeleteFile(File deleteFile) {
        if (!deleteFile.exists()
                || Config.REPOSITORY_PATH_FILE.equals(deleteFile)) {
            return;
        }

        boolean isDirectory = deleteFile.isDirectory();

        if (isDirectory && !DELETE_DIRECTORY_FILEFILTER.accept(deleteFile)) {
            return;
        }

        try {
            FileUtils.forceDelete(deleteFile);
            logDeleteText(deleteFile, null);
        } catch (Throwable throwable) {
            logDeleteText(deleteFile, throwable);
        }

        if (isDirectory) {
            doDeleteFile(deleteFile.getParentFile());
        }
    }

    private static void logDeleteText(File deleteFile, Throwable throwable) {
        String filePath;
        try {
            filePath = deleteFile.getCanonicalPath();
        } catch (IOException ignored) {
            filePath = deleteFile.getAbsolutePath();
        }

        if (throwable == null) {
            System.out.println("SUCCESS:" + filePath);
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            throwable.printStackTrace(pw);
            System.err.println("FAILURE:" + filePath
                    + System.lineSeparator()
                    + sw.toString());
        }
    }

}
