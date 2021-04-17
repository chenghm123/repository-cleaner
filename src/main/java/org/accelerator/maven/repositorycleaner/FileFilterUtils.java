package org.accelerator.maven.repositorycleaner;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

public abstract class FileFilterUtils extends org.apache.commons.io.filefilter.FileFilterUtils implements Config {

    public static IOFileFilter deleteFileFileFilter() {
        OrFileFilter ioFileFilter = new OrFileFilter();

        if (FILE_TYPE_OPERATION != Operation.NONE) {
            IOFileFilter fileTypeFilter = new SuffixFileFilter(FILE_TYPE, IOCase.INSENSITIVE);
            if (FILE_TYPE_OPERATION == Operation.INCLUDE) {
                fileTypeFilter = notFileFilter(fileTypeFilter);
            }
            ioFileFilter.addFileFilter(fileTypeFilter);
        }

        if (FILE_NAME_OPERATION != Operation.NONE) {
            IOFileFilter fileNameFilter = new RegexFileFilter(FILE_NAME_REGEX);
            if (FILE_NAME_OPERATION == Operation.INCLUDE) {
                fileNameFilter = notFileFilter(fileNameFilter);
            }
            ioFileFilter.addFileFilter(fileNameFilter);
        }

        return ioFileFilter;
    }

    public static IOFileFilter deleteDirectoryFileFilter() {
        OrFileFilter ioFileFilter = new OrFileFilter();

        if (DIRECTORY_NAME_OPERATION != Operation.NONE) {
            IOFileFilter directoryNameFilter = new RegexFileFilter(DIRECTORY_NAME_REGEX);
            if (DIRECTORY_NAME_OPERATION == Operation.INCLUDE) {
                directoryNameFilter = notFileFilter(directoryNameFilter);
            }
            ioFileFilter.addFileFilter(directoryNameFilter);
        }

        if (DELETE_EMPTY_DIRECTORY) {
            ioFileFilter.addFileFilter(EmptyFileFilter.EMPTY);
        }

        return ioFileFilter;
    }

}
