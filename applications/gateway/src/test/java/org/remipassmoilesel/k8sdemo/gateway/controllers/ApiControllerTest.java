package org.remipassmoilesel.k8sdemo.gateway.controllers;

import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.k8sdemo.TestHelpers;
import org.remipassmoilesel.k8sdemo.clients.signature.SignatureClient;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.gateway.Application;
import org.remipassmoilesel.k8sdemo.gateway.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO: verify values

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(Application.TEST_PROFILE)
public class ApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private SignatureClient signatureClient;

    @Autowired
    private ApiController apiController;

    private SignedDocument testDoc;

    @Before
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();

        testDoc = TestHelpers.getTestDocument(1);
        testDoc.setId("test-id");
        testDoc.setSignature("test-signature");
    }

    @Test
    public void getAppIdentity() throws Exception {
        mockMvc.perform(get(Routes.APP_IDENTITY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.hostname").exists())
                .andExpect(jsonPath("$.envVars").exists())
                .andReturn();
    }

    @Test
    public void postDocument() throws Exception {
        reset(signatureClient);
        when(signatureClient.persistAndSignDocument(any(SignedDocument.class))).thenReturn(Single.just(testDoc));

        MockMultipartFile testDocument = new MockMultipartFile(
                "document",
                "document.odt",
                "application/vnd.oasis.opendocument.text ",
                TestHelpers.getTestDocument(1).getContent()
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(Routes.DOCUMENTS).file(testDocument)).andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.signature").exists())
                .andExpect(jsonPath("$.content").doesNotExist())
                .andReturn();

        verify(signatureClient).persistAndSignDocument(any(SignedDocument.class));
    }

    @Test
    public void getDocuments() throws Exception {
        reset(signatureClient);
        when(signatureClient.getDocuments()).thenReturn(Single.just(Arrays.asList(testDoc)));

        MvcResult result = mockMvc.perform(get(Routes.DOCUMENTS)).andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].signature").exists())
                .andExpect(jsonPath("$[0].content").doesNotExist())
                .andReturn();

        verify(signatureClient).getDocuments();
    }

    @Test
    public void deleteDocumentsShouldWork() throws Exception {
        reset(signatureClient);
        when(signatureClient.deleteDocument(any(String.class))).thenReturn(Single.just(new MCMessage()));

        MvcResult result = mockMvc.perform(delete(Routes.DOCUMENTS).param("documentId", "test-id"))
                .andReturn();

        mockMvc.perform(asyncDispatch(result)).andExpect(status().isOk());

        verify(signatureClient).deleteDocument("test-id");
    }

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