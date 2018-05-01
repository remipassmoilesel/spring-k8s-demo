package org.remipassmoilesel.k8sdemo.gateway.controllers;

import io.reactivex.Single;
import org.apache.commons.io.IOUtils;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.SignatureClient;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.gateway.Routes;
import org.remipassmoilesel.k8sdemo.gateway.app_identity.GatewayIdentity;
import org.remipassmoilesel.k8sdemo.gateway.app_identity.GatewayIdentityProvider;
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
    private SignatureClient signatureClient;

    @Autowired
    private GatewayIdentityProvider gatewayIdentityProvider;

    @ResponseBody
    @RequestMapping(
            path = Routes.APP_IDENTITY,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public GatewayIdentity getAppIdentity() {

        return gatewayIdentityProvider.getGatewayIdentity();

    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOCUMENTS,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Single<List<SignedDocument>> getDocuments() {
        return signatureClient.getDocuments()
                .map((list) -> {
                    // do not send document content as it is binary
                    list.forEach(doc -> doc.setContent(null));
                    return list;
                });
    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOCUMENTS,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Single<SignedDocument> uploadAndSignDocument(
            @RequestParam("document") MultipartFile sentDocument) throws IOException {

        InputStream docInputStream = sentDocument.getInputStream();
        String documentName = sentDocument.getOriginalFilename();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        SignedDocument document = new SignedDocument();
        document.setName(documentName);
        document.setContent(documentContent);

        return signatureClient.persistAndSignDocument(document)
                .map(doc -> {
                    // do not send document content as it is binary
                    doc.setContent(null);
                    return doc;
                });
    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOCUMENTS,
            method = RequestMethod.DELETE
    )
    public Single<Boolean> deleteDocument(@RequestParam("documentId") String documentId) {
        return signatureClient.deleteDocument(documentId);
    }

    @ResponseBody
    @RequestMapping(
            path = Routes.DOC_SIGNATURE,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Single<GpgValidationResult> checkIfDocumentValid(
            @RequestParam("candidate") MultipartFile sentCandidate,
            @RequestParam("documentId") String documentId) throws IOException {

        InputStream docInputStream = sentCandidate.getInputStream();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        SignedDocument document = new SignedDocument();
        document.setContent(documentContent);

        return signatureClient.checkDocument(document, documentId)
                .map((result) -> {
                    // do not send document content as it is binary
                    result.getDocument().setContent(null);
                    return result;
                });

    }
}
