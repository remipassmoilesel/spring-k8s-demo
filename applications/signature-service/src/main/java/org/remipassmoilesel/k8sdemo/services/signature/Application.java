package org.remipassmoilesel.k8sdemo.services.signature;

import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static final String DEV_PROFILE = "dev";
    public static final String PROD_PROFILE = "prod";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${app.microcomm.context}")
    private String microCommContext;

    @Value("${app.microcomm.natsUrl}")
    private String microCommNatsUrl;

    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(Application.class);
        springApp.run(args);
    }

    @Bean
    public MicroCommSync createComm() {
        MicroCommSyncConfig config = new MicroCommSyncConfig(microCommNatsUrl, microCommContext);
        return new MicroCommSync(config);
    }

}
