package com.paulribe.memowords.common.enumeration;

import android.content.Context;

import com.paulribe.memowords.R;

import androidx.annotation.NonNull;

public enum LanguageEnum {
    PORTUGUESE("portuguese", R.string.portuguese),
    GERMAN("german", R.string.german),
    ENGLISH("english", R.string.english),
    FRENCH("french", R.string.french),
    SPANISH("spanish", R.string.spanish),
    NONE("select your native language", R.string.select_language);

    private final String language;
    private final int languageCode;

    LanguageEnum(String value, int languageCode) {
        language = value;
        this.languageCode = languageCode;
    }

    public String getLanguage() {
        return language;
    }

    @NonNull
    public String toString(Context context) {
        return context.getResources().getString(languageCode);
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
