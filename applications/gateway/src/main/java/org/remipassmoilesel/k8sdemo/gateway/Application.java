package org.remipassmoilesel.k8sdemo.gateway;

import org.remipassmoilesel.k8sdemo.clients.signature.SignatureClient;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static final String DEV_PROFILE = "dev";
    public static final String PROD_PROFILE = "prod";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${app.microcomm.context}")
    private String microCommContext;

    @Value("${app.microcomm.natsUrl}")
    private String microCommNatsUrl;

    public static void main(String[] args) {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(MicroCommSync.class);
        rootLogger.setLevel(Level.TRACE);

        SpringApplication springApp = new SpringApplication(Application.class);
        springApp.run(args);
    }

    @Bean
    public MicroCommSync createComm() throws IOException {
        return MicroCommSync.connectFromParameters(microCommNatsUrl, microCommContext);
    }

    // FIXME: fix scan for components in dependencies
    @Bean
    public SignatureClient createSignatureClient() throws IOException {
        SignatureClient client = new SignatureClient(createComm());
        return client;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                if (activeProfile.equals(Application.DEV_PROFILE)) {
                    registry.addMapping("/**")
                            .allowedOrigins("*")
                            .allowedMethods("*")
                            .allowedHeaders("*");
                }
            }
        };
    }
}