package com.paulribe.memowords.model;

public class UserConfig {
    private String currentLanguage;

    public UserConfig() { }

    public UserConfig(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }
}
