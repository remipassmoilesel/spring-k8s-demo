package org.remipassmoilesel.k8sdemo.services.signature;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class Application implements ApplicationListener<ApplicationReadyEvent> {

    public static final String DEV_PROFILE = "dev";
    public static final String PROD_PROFILE = "prod";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${app.microcomm.context}")
    private String microCommContext;

    @Value("${app.microcomm.natsUrl}")
    private String microCommNatsUrl;

    @Autowired
    private SignatureServer signatureServer;

    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(Application.class);
        springApp.run(args);
    }

    @Bean
    public MicroCommSync createComm() throws IOException {
        return MicroCommSync.connectFromParameters(microCommNatsUrl, microCommContext);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.signatureServer.registerHandlers();
    }
}
