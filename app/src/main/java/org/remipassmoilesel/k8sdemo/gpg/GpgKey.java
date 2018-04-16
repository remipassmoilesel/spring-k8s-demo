package org.remipassmoilesel.k8sdemo.gpg;

import java.util.Objects;

public class GpgKey {

    private String name;
    private String email;
    private String privateKey;
    private String publicKey;

    public GpgKey(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GpgKey gpgKey = (GpgKey) o;
        return Objects.equals(name, gpgKey.name) &&
                Objects.equals(email, gpgKey.email) &&
                Objects.equals(privateKey, gpgKey.privateKey) &&
                Objects.equals(publicKey, gpgKey.publicKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, email, privateKey, publicKey);
    }

    @Override
    public String toString() {
        return "GpgKey{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
