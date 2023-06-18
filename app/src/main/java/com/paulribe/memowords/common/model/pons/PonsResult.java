package com.paulribe.memowords.common.model.pons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PonsResult {

    @SerializedName("lang")
    private String lang;
    @SerializedName("hits")
    private List<SearchWordResultList> hits;

    public PonsResult(String lang, List<SearchWordResultList> hits) {
        this.lang = lang;
        this.hits = hits;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<SearchWordResultList> getHits() {
        return hits;
    }

    public void setHits(List<SearchWordResultList> hits) {
        this.hits = hits;
    }
}
