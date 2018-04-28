package org.remipassmoilesel.microservice_commons.sync;

import org.remipassmoilesel.microservice_commons.common.Helpers;

import java.util.Objects;
import java.util.logging.Level;

public class MicroCommSyncConfig {

    private final String url;
    private final String context;

    public MicroCommSyncConfig(String context, String url) {
        Helpers.checkSubjectString(context);
        this.context = context;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "MicroCommSyncConfig{" +
                "url='" + url + '\'' +
                ", context='" + context + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MicroCommSyncConfig that = (MicroCommSyncConfig) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, context);
    }
}
