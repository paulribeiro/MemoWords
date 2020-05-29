package com.paulribe.memowords.model.pons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchWordResult {

    @SerializedName("headword")
    private String headword;
    @SerializedName("headword_full")
    private String headwordfull;
    @SerializedName("wordclass")
    private String wordclass;
    @SerializedName("arabs")
    private List<WordMeaning> arabs;

    public SearchWordResult(String headword, String headwordfull, String wordclass, List<WordMeaning> wordMeanings) {
        this.headword = headword;
        this.headwordfull = headwordfull;
        this.wordclass = wordclass;
        this.arabs = wordMeanings;
    }

    public String getHeadword() {
        return headword;
    }

    public void setHeadword(String headword) {
        this.headword = headword;
    }

    public String getHeadwordfull() {
        return headwordfull;
    }

    public void setHeadwordfull(String headwordfull) {
        this.headwordfull = headwordfull;
    }

    public String getWordclass() {
        return wordclass;
    }

    public void setWordclass(String wordclass) {
        this.wordclass = wordclass;
    }

    public List<WordMeaning> getArabs() {
        return arabs;
    }

    public void setArabs(List<WordMeaning> wordMeanings) {
        this.arabs = wordMeanings;
    }
}
