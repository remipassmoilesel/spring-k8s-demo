package org.remipassmoilesel.k8sdemo.gateway.app_identity;

import org.springframework.stereotype.Component;

@Component
public class GatewayIdentityProvider {

    private GatewayIdentity gatewayIdentity;

    public GatewayIdentity getGatewayIdentity() {
        if (this.gatewayIdentity == null) {
            this.gatewayIdentity = new GatewayIdentity();
        }
        return new GatewayIdentity();
    }

}
