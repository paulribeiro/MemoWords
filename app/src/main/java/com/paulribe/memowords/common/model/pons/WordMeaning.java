package com.paulribe.memowords.common.model.pons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WordMeaning {

    @SerializedName("header")
    private String header;
    @SerializedName("translations")
    private List<Translation> translations;

    public WordMeaning(String header, List<Translation> translations) {
        this.header = header;
        this.translations = translations;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}
