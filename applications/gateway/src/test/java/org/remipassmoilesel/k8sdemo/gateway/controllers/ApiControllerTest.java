package org.remipassmoilesel.k8sdemo.gateway.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.k8sdemo.TestHelpers;
import org.remipassmoilesel.k8sdemo.clients.signature.SignatureClient;
import org.remipassmoilesel.k8sdemo.gateway.Application;
import org.remipassmoilesel.k8sdemo.gateway.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(Application.DEV_PROFILE)
public class ApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private SignatureClient signatureClient;

    @Autowired
    private ApiController apiController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();
    }

    @Test
    public void getAppIdentityShouldWork() throws Exception {
        mockMvc.perform(get(Routes.APP_IDENTITY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.hostname").exists())
                .andExpect(jsonPath("$.envVars").exists())
                .andReturn();
    }

    @Test
    public void postDocumentShouldWork() throws Exception {

        MockMultipartFile testDocument = new MockMultipartFile(
                "document",
                "document.odt",
                "application/vnd.oasis.opendocument.text ",
                TestHelpers.getTestDocument(1).getContent()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart(Routes.DOCUMENTS).file(testDocument))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.signature").exists())
                .andReturn();
    }

//
//    @Test
//    public void getDocumentsShouldWork() throws Exception {
//
//        SignedDocument testDoc = TestHelpers.getTestDocument(1);
//        documentManager.persistDocument(testDoc.getName(), testDoc.getContent());
//
//        mockMvc.perform(get(Routes.DOCUMENTS))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
//                .andExpect(jsonPath("$[0].id").exists())
//                .andExpect(jsonPath("$[0].name").exists())
//                .andExpect(jsonPath("$[0].date").exists())
//                .andExpect(jsonPath("$[0].signature").exists())
//                .andReturn();
//    }
//
//    @Test
//    public void deleteDocumentsShouldWork() throws Exception {
//
//        Document testDoc = TestHelpers.getTestDocument(1);
//        Document fullDoc = documentManager.persistDocument(testDoc.getName(), testDoc.getContent());
//
//        mockMvc.perform(delete(Routes.DOCUMENTS)
//                .param("documentId", String.valueOf(fullDoc.getId())))
//                .andExpect(status().isOk())
//                .andReturn();
//
//    }
//
//    @Test
//    public void checkDocumentShouldValidateDocIfValid() throws Exception {
//
//        Document testDoc = TestHelpers.getTestDocument(1);
//        Document completeDoc = documentManager.persistDocument(testDoc.getName(), testDoc.getContent());
//
//        MockMultipartFile testDocument = new MockMultipartFile(
//                "candidate",
//                "document.odt",
//                "application/vnd.oasis.opendocument.text",
//                testDoc.getContent()
//        );
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart(Routes.DOC_SIGNATURE)
//                .file(testDocument)
//                .param("documentId", String.valueOf(completeDoc.getId())))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$.valid", is(true)))
//                .andExpect(jsonPath("$.document").exists())
//                .andReturn();
//    }
//
//    @Test
//    public void checkDocumentShouldInvalidateDocIfInvalid() throws Exception {
//
//        Document testDoc = TestHelpers.getTestDocument(1);
//        Document testDoc2 = TestHelpers.getTestDocument(2);
//        Document completeDoc = documentManager.persistDocument(testDoc.getName(), testDoc.getContent());
//
//        MockMultipartFile testDocument = new MockMultipartFile(
//                "candidate",
//                "document.odt",
//                "application/vnd.oasis.opendocument.text",
//                testDoc2.getContent()
//        );
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart(Routes.DOC_SIGNATURE)
//                .file(testDocument)
//                .param("documentId", String.valueOf(completeDoc.getId())))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$.valid", is(false)))
//                .andExpect(jsonPath("$.document").exists())
//                .andReturn();
//    }

}