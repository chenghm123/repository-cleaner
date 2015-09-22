import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO

scan("60 seconds")
def APP_NAME = "repository-cleaner"
def LOG_HOME = "logs"
def ENCODER_PATTERN = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger - %msg%n"
context.name = "${APP_NAME}"
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "${ENCODER_PATTERN}"
    }
}
appender("FILE", RollingFileAppender) {
    file = "${APP_NAME}.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_HOME}/${APP_NAME}/%d{yyyy-MM-dd}.log"
        maxHistory = 75
    }
    encoder(PatternLayoutEncoder) {
        pattern = "${ENCODER_PATTERN}"
    }
}
logger("org.accelerator.maven.repositorycleaner", DEBUG, [], true)
root(INFO, ["FILE", "STDOUT"])