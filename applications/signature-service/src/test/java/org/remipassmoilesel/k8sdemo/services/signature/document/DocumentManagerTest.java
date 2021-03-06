package org.remipassmoilesel.k8sdemo.services.signature.document;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.remipassmoilesel.k8sdemo.services.signature.Application;
import org.remipassmoilesel.k8sdemo.services.signature.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(Application.DEV_PROFILE)
public class DocumentManagerTest {

    @Autowired
    private DocumentManager documentManager;

    @Test
    public void getDocumentShouldWork() throws Exception {

        SignedDocument testDoc = TestHelpers.getTestDocument(1);
        SignedDocument persisted = documentManager.persistDocument(testDoc);

        SignedDocument retrieved = documentManager.getDocumentById(persisted.getId());

        assertThat(retrieved.getId(), notNullValue());
        assertThat(retrieved.getId(), equalTo(persisted.getId()));
        assertThat(retrieved.getName(), equalTo(persisted.getName()));
        assertThat(retrieved.getDate(), equalTo(persisted.getDate()));
        assertThat(retrieved.getContent(), equalTo(persisted.getContent()));
        assertThat(retrieved.getSignature(), equalTo(persisted.getSignature()));

    }

    @Test(expected = DocumentNotFoundException.class)
    public void deleteDocumentsShouldWork() throws Exception {

        SignedDocument testDoc = TestHelpers.getTestDocument(1);
        SignedDocument persisted = documentManager.persistDocument(testDoc);

        documentManager.deleteDocument(persisted.getId());
        documentManager.getDocumentById(persisted.getId());
    }

    @Test
    public void checkDocumentShouldValidateDocIfValid() throws Exception {

        SignedDocument testDoc = TestHelpers.getTestDocument(1);
        SignedDocument persisted = documentManager.persistDocument(testDoc);

        GpgValidationResult result = documentManager.verifyDocument(persisted.getContent(), persisted.getId());

        assertThat(result.isValid(), is(true));
    }

    @Test
    public void checkDocumentShouldInvalidateDocIfInvalid() throws Exception {

        SignedDocument testDoc = TestHelpers.getTestDocument(1);
        SignedDocument testDoc2 = TestHelpers.getTestDocument(2);
        SignedDocument persisted = documentManager.persistDocument(testDoc);

        GpgValidationResult result = documentManager.verifyDocument(testDoc2.getContent(), persisted.getId());

        assertThat(result.isValid(), is(false));
    }

}
