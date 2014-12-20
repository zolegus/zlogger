package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public class LoggerConfiguration {
    private ZLogLevel level = ZLogLevel.DEBUG;
    private String basePath = "./logs";

    public ZLogLevel getLevel() {
        return level;
    }

    public void setLevel(ZLogLevel level) {
        this.level = level;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
