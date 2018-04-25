package org.remipassmoilesel.microservice_commons.sync;

import org.remipassmoilesel.microservice_commons.common.Helpers;

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

}
