package org.accelerator.maven.repositorycleaner.filefilter;

import java.io.File;

import org.accelerator.maven.repositorycleaner.Config;
import org.accelerator.maven.repositorycleaner.Operation;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

public class DeleteFileFileFilter extends AbstractFileFilter {

    private IOFileFilter deleteFileFileFilter;

    public DeleteFileFileFilter() {
        IOFileFilter fileTypeFilter = buildFileTypeFilter();
        IOFileFilter fileNameFilter = buildFileNameFilter();
        if (fileTypeFilter != null && fileNameFilter != null)
            deleteFileFileFilter = FileFilterUtils.or(fileTypeFilter, fileNameFilter);
        else if (fileTypeFilter != null)
            deleteFileFileFilter = fileTypeFilter;
        else if (fileNameFilter != null)
            deleteFileFileFilter = fileNameFilter;
        else
            deleteFileFileFilter = FileFilterUtils.falseFileFilter();
    }

    /**
     * 构建FileTypeFilter
     *
     * @return fileTypeFilter
     */
    private IOFileFilter buildFileTypeFilter() {
        IOFileFilter fileTypeFilter = null;
        Operation fileTypeOperation = Config.FILE_TYPE_OPERATION();
        if (fileTypeOperation != Operation.NONE) {
            fileTypeFilter = new SuffixFileFilter(Config.FILE_TYPE(), IOCase.INSENSITIVE);
            if (fileTypeOperation == Operation.INCLUDE)
                fileTypeFilter = FileFilterUtils.notFileFilter(fileTypeFilter);
        }
        return fileTypeFilter;
    }

    /**
     * 构建FileNameFilter
     *
     * @return fileNameFilter
     */
    private IOFileFilter buildFileNameFilter() {
        IOFileFilter fileNameFilter = null;
        Operation fileNameOperation = Config.FILE_NAME_OPERATION();
        if (fileNameOperation != Operation.NONE) {
            fileNameFilter = new RegexFileFilter(Config.FILE_NAME_REGEX());
            if (fileNameOperation == Operation.INCLUDE)
                fileNameFilter = FileFilterUtils.notFileFilter(fileNameFilter);
        }
        return fileNameFilter;
    }

    @Override
    public boolean accept(File file) {
        return deleteFileFileFilter.accept(file);
    }

}
