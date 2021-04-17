package org.accelerator.maven.repositorycleaner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface Config {

    File REPOSITORY_PATH_FILE = new File(System.getProperty("user.home"), ".m2/repository");

    Operation FILE_TYPE_OPERATION = Operation.INCLUDE;
    List<String> FILE_TYPE = Arrays.asList("jar", "pom", "md5", "sha1", "properties", "xml");

    Operation FILE_NAME_OPERATION = Operation.NONE;
    String FILE_NAME_REGEX = "";

    Operation DIRECTORY_NAME_OPERATION = Operation.EXCLUDE;
    String DIRECTORY_NAME_REGEX = ".cache|.locks|[\\S\\s]*\\$[\\S\\s]*|[\\S\\s]*\\{[\\S\\s]*|[\\S\\s]*\\}[\\S\\s]*|accelerator";

    boolean DELETE_EMPTY_DIRECTORY = true;

}
