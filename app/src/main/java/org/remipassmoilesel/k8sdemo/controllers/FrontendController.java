package org.remipassmoilesel.k8sdemo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.remipassmoilesel.k8sdemo.FrontendConfig;
import org.remipassmoilesel.k8sdemo.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FrontendController {
    private static final Logger logger = LoggerFactory.getLogger(FrontendController.class);

    public static final String INDEX_TEMPLATE_NAME = "index";
    public static final String APP_CONFIG_ATTR = "appConfig";

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = Routes.ROOT)
    public String getFrontend(Model model) throws JsonProcessingException {

        String config = mapper.writeValueAsString(new FrontendConfig());
        model.addAttribute(APP_CONFIG_ATTR, config);

        return INDEX_TEMPLATE_NAME;
    }

}
