package org.remipassmoilesel.k8sdemo.gateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.remipassmoilesel.k8sdemo.gateway.FrontendConfig;
import org.remipassmoilesel.k8sdemo.gateway.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {
    private static final Logger logger = LoggerFactory.getLogger(FrontendController.class);

    public static final String APP_CONFIG_ATTR = "appConfig";
    private static final String INDEX_TEMPLATE_NAME = "index";

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = Routes.ROOT)
    public String getFrontend(Model model) throws JsonProcessingException {

        String config = mapper.writeValueAsString(new FrontendConfig());
        model.addAttribute(APP_CONFIG_ATTR, config);

        return INDEX_TEMPLATE_NAME;

    }

}
