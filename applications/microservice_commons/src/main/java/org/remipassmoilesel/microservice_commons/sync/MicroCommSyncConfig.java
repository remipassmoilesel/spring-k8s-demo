package org.remipassmoilesel.microservice_commons.sync;

import org.remipassmoilesel.microservice_commons.common.Helpers;

import java.util.Objects;
import java.util.logging.Level;

public class MicroCommSyncConfig {

    private static final Level DEFAULT_LOG_LEVEL = Level.FINEST;
    private final String url;
    private final String context;
    private Level logLevel;

    public MicroCommSyncConfig(String context, String url) {
        Helpers.checkSubjectString(context);
        this.context = context;
        this.url = url;
        this.logLevel = DEFAULT_LOG_LEVEL;
    }

    public String getUrl() {
        return url;
    }

    public String getContext() {
        return context;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public String toString() {
        return "MicroCommSyncConfig{" +
                "url='" + url + '\'' +
                ", context='" + context + '\'' +
                ", logLevel=" + logLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MicroCommSyncConfig that = (MicroCommSyncConfig) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(context, that.context) &&
                Objects.equals(logLevel, that.logLevel);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, context, logLevel);
    }
}
