package com.paulribe.memowords.common.model.mymemory;

import com.google.gson.annotations.SerializedName;

public class ResponseData {

    @SerializedName("translatedText")
    private String translatedText;

    @SerializedName("match")
    private Float match;

    public ResponseData() {
    }

    public ResponseData(String translatedText, Float match) {
        this.translatedText = translatedText;
        this.match = match;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public Float getMatch() {
        return match;
    }

    public void setMatch(Float match) {
        this.match = match;
    }
}
