package org.accelerator.maven.repositorycleaner.filefilter;

import java.io.File;

import org.accelerator.maven.repositorycleaner.Config;
import org.accelerator.maven.repositorycleaner.Operation;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class DeleteDirectoryFileFilter extends AbstractFileFilter {

	private IOFileFilter deleteDirectoryFileFilter;

	public DeleteDirectoryFileFilter() {
		IOFileFilter directoryNameFilter = buildDirectoryNameFilter();
		if (Config.DELETE_EMPTY_DIRECTORY() && directoryNameFilter != null)
			deleteDirectoryFileFilter = FileFilterUtils.or(directoryNameFilter, EmptyFileFilter.EMPTY);
		else if (Config.DELETE_EMPTY_DIRECTORY())
			deleteDirectoryFileFilter = EmptyFileFilter.EMPTY;
		else if (directoryNameFilter != null)
			deleteDirectoryFileFilter = directoryNameFilter;
		else
			deleteDirectoryFileFilter = FileFilterUtils.falseFileFilter();
	}

	private IOFileFilter buildDirectoryNameFilter() {
		IOFileFilter directoryNameFilter = null;
		Operation directoryNameOperation = Config.DIRECTORY_NAME_OPERATION();
		if (directoryNameOperation != Operation.NONE) {
			directoryNameFilter = new RegexFileFilter(Config.DIRECTORY_NAME_REGEX());
			if (directoryNameOperation == Operation.INCLUDE)
				directoryNameFilter = FileFilterUtils.notFileFilter(directoryNameFilter);
		}
		return directoryNameFilter;
	}

	@Override
	public boolean accept(File file) {
		return deleteDirectoryFileFilter.accept(file);
	}

}
