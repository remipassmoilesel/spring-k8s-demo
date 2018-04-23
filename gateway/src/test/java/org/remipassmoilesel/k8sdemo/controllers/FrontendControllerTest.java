package org.remipassmoilesel.k8sdemo.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.k8sdemo.Application;
import org.remipassmoilesel.k8sdemo.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(Application.DEV_PROFILE)
public class FrontendControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private FrontendController frontendController;

    @Before
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.standaloneSetup(frontendController).build();
    }

    @Test
    public void testFrontendService() throws Exception {
        mockMvc.perform(get(Routes.ROOT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(FrontendController.APP_CONFIG_ATTR))
                .andReturn();
    }

}
