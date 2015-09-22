package org.accelerator.maven.repositorycleaner;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public abstract class Config {

    protected static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static String REPOSITORY_PATH;

    private static Operation FILE_TYPE_OPERATION;
    private static List<String> FILE_TYPE;

    private static Operation FILE_NAME_OPERATION;
    private static String FILE_NAME_REGEX;

    private static Operation DIRECTORY_NAME_OPERATION;
    private static String DIRECTORY_NAME_REGEX;

    private static boolean DELETE_EMPTY_DIRECTORY;

    public static final String REPOSITORY_PATH() {
        return REPOSITORY_PATH;
    }

    public static final Operation FILE_TYPE_OPERATION() {
        return FILE_TYPE_OPERATION;
    }

    public static final List<String> FILE_TYPE() {
        return FILE_TYPE;
    }

    public static final Operation FILE_NAME_OPERATION() {
        return FILE_NAME_OPERATION;
    }

    public static final String FILE_NAME_REGEX() {
        return FILE_NAME_REGEX;
    }

    public static final Operation DIRECTORY_NAME_OPERATION() {
        return DIRECTORY_NAME_OPERATION;
    }

    public static final String DIRECTORY_NAME_REGEX() {
        return DIRECTORY_NAME_REGEX;
    }

    public static final boolean DELETE_EMPTY_DIRECTORY() {
        return DELETE_EMPTY_DIRECTORY;
    }

    static {
        try {
            Configuration configuration = new PropertiesConfiguration("config.properties");
            Class<Config> configurationType = Config.class;
            Field[] fields = configurationType.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                if (String.class.isAssignableFrom(fieldType)) {
                    String fieldValue = configuration.getString(fieldName);
                    field.set(null, fieldValue);
                } else if (Operation.class.isAssignableFrom(fieldType)) {
                    String fieldValueName = configuration.getString(fieldName);
                    fieldValueName = fieldValueName.toUpperCase();
                    Operation fieldValue = Operation.valueOf(fieldValueName);
                    field.set(null, fieldValue);
                } else if (Collection.class.isAssignableFrom(fieldType)) {
                    List<?> fieldValue = configuration.getList(fieldName);
                    field.set(null, fieldValue);
                } else if (boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)) {
                    boolean fieldValue = configuration.getBoolean(fieldName);
                    field.setBoolean(null, fieldValue);
                } else {
                    continue;
                }
            }
            if (StringUtils.isEmpty(REPOSITORY_PATH))
                REPOSITORY_PATH = SystemUtils.getUserHome().getCanonicalPath()
                        + File.separator + ".m2" + File.separator + "repository";
        } catch (ConfigurationException | IllegalArgumentException | IllegalAccessException | IOException e) {
            logger.error("初始化配置文件失败！", e);
        }
    }
}
