package org.remipassmoilesel.k8sdemo.controllers;

import org.apache.commons.io.IOUtils;
import org.remipassmoilesel.k8sdemo.Routes;
import org.remipassmoilesel.k8sdemo.app_identity.AppIdentity;
import org.remipassmoilesel.k8sdemo.app_identity.AppIdentityProvider;
import org.remipassmoilesel.k8sdemo.signature.document.Document;
import org.remipassmoilesel.k8sdemo.signature.document.DocumentManager;
import org.remipassmoilesel.k8sdemo.signature.gpg.GpgValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private AppIdentityProvider appIdentityProvider;

    @ResponseBody
    @RequestMapping(
            path = Routes.APP_IDENTITY,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AppIdentity getAppIdentity() {

        return appIdentityProvider.getAppIdentity();

    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOCUMENTS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<Document> getDocuments() throws IOException {

        List<Document> documents = documentManager.getDocuments();

        // do not send document content
        documents.stream().forEach(doc -> doc.setContent(null));
        return documents;

    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOCUMENTS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Document uploadAndSignDocument(
            @RequestParam("document") MultipartFile sentDocument) throws IOException {

        InputStream docInputStream = sentDocument.getInputStream();
        String documentName = sentDocument.getOriginalFilename();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        Document document = documentManager.persistDocument(documentName, documentContent);

        // do not send document content
        document.setContent(null);
        return document;

    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOCUMENTS,
            method = RequestMethod.DELETE
    )
    public void deleteDocument(@RequestParam("documentId") Long documentId) {
        documentManager.deleteDocument(documentId);
    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOC_SIGNATURE,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public GpgValidationResult checkIfDocumentValid(
            @RequestParam("candidate") MultipartFile sentCandidate,
            @RequestParam("documentId") Long documentId) throws IOException {

        InputStream docInputStream = sentCandidate.getInputStream();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        GpgValidationResult validationResult = documentManager.verifyDocument(documentContent, documentId);

        // do not send document content
        validationResult.getDocument().setContent(null);
        return validationResult;

    }
}
