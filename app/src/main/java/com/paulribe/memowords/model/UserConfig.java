package com.paulribe.memowords.model;

public class UserConfig {

    private String currentLanguage;
    private String nativeLanguage;
    private String xApiKeyPons;

    public UserConfig() { }

    public UserConfig(String currentLanguage, String nativeLanguage, String xapiKeyPons) {
        this.currentLanguage = currentLanguage;
        this.nativeLanguage = nativeLanguage;
        this.xApiKeyPons = xapiKeyPons;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getXApiKeyPons() {
        return xApiKeyPons;
    }

    public void setXApiKeyPons(String xapiKeyPons) {
        this.xApiKeyPons = xapiKeyPons;
    }
}
