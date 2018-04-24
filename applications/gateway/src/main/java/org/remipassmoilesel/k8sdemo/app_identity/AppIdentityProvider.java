package org.remipassmoilesel.k8sdemo.app_identity;

import org.springframework.stereotype.Component;

@Component
public class AppIdentityProvider {

    private AppIdentity appIdentity;

    public AppIdentity getAppIdentity() {
        if (this.appIdentity == null) {
            this.appIdentity = new AppIdentity();
        }
        return new AppIdentity();
    }

}
