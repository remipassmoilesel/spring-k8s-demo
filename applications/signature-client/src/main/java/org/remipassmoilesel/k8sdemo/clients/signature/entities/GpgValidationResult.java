package org.remipassmoilesel.k8sdemo.clients.signature.entities;

import java.io.Serializable;
import java.util.Objects;

public class GpgValidationResult implements Serializable {

    private boolean isValid;
    private SignedDocument document;

    public GpgValidationResult(SignedDocument document, boolean isValid) {
        this.document = document;
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public SignedDocument getDocument() {
        return document;
    }

    public void setDocument(SignedDocument document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GpgValidationResult that = (GpgValidationResult) o;
        return isValid == that.isValid &&
                Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, isValid);
    }

    @Override
    public String toString() {
        return "GpgValidationResult{" +
                "document=" + document +
                ", isValid=" + isValid +
                '}';
    }
}
