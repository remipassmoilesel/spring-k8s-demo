package org.remipassmoilesel.k8sdemo.gateway.app_identity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class GatewayIdentity {
    private static final Logger logger = LoggerFactory.getLogger(GatewayIdentity.class);

    private String hostname;
    private Map<String, String> envVars;

    public GatewayIdentity() {
        this.hostname = this.findHostName();
        this.envVars = System.getenv();
    }

    public String getHostname() {
        return hostname;
    }

    public Map<String, String> getEnvVars() {
        return envVars;
    }

    private String findHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("Unable to find hostname: ", e);
            return "Unknown host";
        }
    }

}
