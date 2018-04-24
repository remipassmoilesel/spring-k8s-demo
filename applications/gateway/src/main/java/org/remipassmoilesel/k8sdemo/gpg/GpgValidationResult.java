package org.remipassmoilesel.k8sdemo.gpg;

import org.remipassmoilesel.k8sdemo.document.Document;

import java.util.Objects;

public class GpgValidationResult {

    private boolean isValid;
    private Document document;

    public GpgValidationResult(Document document, boolean isValid) {
        this.document = document;
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
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
