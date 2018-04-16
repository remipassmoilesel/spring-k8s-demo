package org.remipassmoilesel.k8sdemo.app_identity;

public class AppIdentityProvider {

    private AppIdentity appIdentity;

    public AppIdentity getAppIdentity() {
        if (this.appIdentity == null) {
            this.appIdentity = new AppIdentity();
        }
        return new AppIdentity();
    }

}
