package com.paulribe.memowords.enumeration;

public enum LanguageEnum {
    PORTUGUESE("portuguese"),
    GERMAN("german"),
    ENGLISH("english"),
    FRENCH("french"),
    SPANISH("spanish"),
    NONE("select your native language");

    private final String language;

    private LanguageEnum(String value) {
        language = value;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return language;
    }
}
