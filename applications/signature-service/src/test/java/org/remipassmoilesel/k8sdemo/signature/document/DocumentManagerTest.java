package org.remipassmoilesel.k8sdemo.signature.document;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.k8sdemo.signature.Application;
import org.remipassmoilesel.k8sdemo.signature.test_helpers.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(Application.DEV_PROFILE)
public class DocumentManagerTest {

    @Autowired
    private DocumentManager documentManager;

    @Test
    public void getDocumentsShouldWork() throws Exception {

        SignedDocument testDoc = TestHelpers.getTestDocument(1);
        SignedDocument persisted = documentManager.persistDocument(testDoc.getName(), testDoc.getContent());

        SignedDocument retrieved = documentManager.getDocumentById(persisted.getId());

        assertThat(retrieved.getName(), equalTo(persisted.getName()));
        assertThat(retrieved.getDate(), equalTo(persisted.getDate()));
        assertThat(retrieved.getContent(), equalTo(persisted.getContent()));
        assertThat(retrieved.getSignature(), equalTo(persisted.getSignature()));

    }
}
