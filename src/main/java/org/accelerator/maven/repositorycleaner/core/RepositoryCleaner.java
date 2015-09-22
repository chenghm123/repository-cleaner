package org.accelerator.maven.repositorycleaner.core;

import org.accelerator.maven.repositorycleaner.Config;
import org.accelerator.maven.repositorycleaner.filefilter.DeleteDirectoryFileFilter;
import org.accelerator.maven.repositorycleaner.filefilter.DeleteFileFileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public abstract class RepositoryCleaner {

    protected static final Logger logger = LoggerFactory.getLogger(RepositoryCleaner.class);

    private static final File REPOSITORY_PATH_FILE = new File(Config.REPOSITORY_PATH());

    private static final IOFileFilter DELETE_FILE_FILEFILTER = new DeleteFileFileFilter();

    private static final IOFileFilter DELETE_DIRECTORY_FILEFILTER = new DeleteDirectoryFileFilter();

    public static final void startCleaner() {
        Collection<File> deleteFileCollection = FileUtils.listFilesAndDirs(
                REPOSITORY_PATH_FILE, DELETE_FILE_FILEFILTER, FileFilterUtils.trueFileFilter());
        deleteFileCollection.remove(REPOSITORY_PATH_FILE);
        for (File deleteFile : deleteFileCollection)
            doDeleteFile(deleteFile);
    }

    private static final void doDeleteFile(File deleteFile) {
        if (!deleteFile.exists())
            return;
        boolean isDirectory = deleteFile.isDirectory();
        if (isDirectory && !DELETE_DIRECTORY_FILEFILTER.accept(deleteFile))
            return;
        deleteFileAndPrintLog(deleteFile);
        if (isDirectory) {
            File parentFile = deleteFile.getParentFile();
            while (DELETE_DIRECTORY_FILEFILTER.accept(parentFile)) {
                deleteFileAndPrintLog(parentFile);
                parentFile = parentFile.getParentFile();
            }
        }
    }

    private static final void deleteFileAndPrintLog(File deleteFile) {
        boolean isDirectory = deleteFile.isDirectory();
        try {
            FileUtils.forceDelete(deleteFile);
            OutputDeleteLog(deleteFile, true, isDirectory);
        } catch (Exception e) {
            OutputDeleteLog(deleteFile, false, isDirectory);
        }
    }

    private static final void OutputDeleteLog(File deleteFile, boolean status, boolean isDirectory) {
        String state = status ? "SUCCESS" : "FAILURE";
        String fileType = isDirectory ? "Directory" : "File";
        String filePath;
        try {
            filePath = deleteFile.getCanonicalPath();
        } catch (IOException e) {
            filePath = deleteFile.getAbsolutePath();
        }
        StringBuilder loggerBuilder = new StringBuilder();
        loggerBuilder.append(SystemUtils.LINE_SEPARATOR);
        loggerBuilder.append("DELState:[{}]");
        loggerBuilder.append(SystemUtils.LINE_SEPARATOR);
        loggerBuilder.append("FileType:[{}]");
        loggerBuilder.append(SystemUtils.LINE_SEPARATOR);
        loggerBuilder.append("FilePath:[{}]");
        String loggerStr = loggerBuilder.toString();
        if (status)
            logger.info(loggerStr, state, fileType, filePath);
        else
            logger.error(loggerStr, state, fileType, filePath);
    }
}
