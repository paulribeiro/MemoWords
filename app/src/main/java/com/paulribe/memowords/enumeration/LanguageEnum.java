package com.paulribe.memowords.enumeration;

public enum LanguageEnum {
    PORTUGUESE("portuguese"),
    GERMAN("german"),
    ENGLISH("english");

    private final String language;

    private LanguageEnum(String value) {
        language = value;
    }

    public String getLanguage() {
        return language;
    }
}
