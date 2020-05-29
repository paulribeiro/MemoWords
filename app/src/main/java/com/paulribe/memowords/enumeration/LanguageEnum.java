package com.paulribe.memowords.enumeration;

import androidx.annotation.NonNull;

public enum LanguageEnum {
    PORTUGUESE("portuguese"),
    GERMAN("german"),
    ENGLISH("english"),
    FRENCH("french"),
    SPANISH("spanish"),
    NONE("select your native language");

    private final String language;

    LanguageEnum(String value) {
        language = value;
    }

    public String getLanguage() {
        return language;
    }

    @NonNull
    public String toString() {
        return language;
    }

    public String getPrefixForPons(){
        switch(this) {
            case FRENCH:
                return "fr";
            case GERMAN:
                return "de";
            case ENGLISH:
                return "en";
            case SPANISH:
                return "es";
            case PORTUGUESE:
                return "pt";
            default:
                return "";
        }
    }
}
