package com.paulribe.memowords.common.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Word {
    private String wordId;
    private String wordNative;
    private String wordTranslated;
    private long dateAdded;
    private Long lastSuccess;
    private Long lastTry;
    private Integer numberTry;
    private Integer numberSuccess;
    private String context;
    private Integer knowledgeLevel;
    private boolean isFavorite;

    public Word() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Word(String wordNative, String wordTranslated, long dateAdded, Long lastSuccess, Long lastTry, Integer numberTry, Integer numberSuccess, String context, Integer knowledgeLevel, boolean isFavorite) {
        this.wordNative = wordNative;
        this.wordTranslated = wordTranslated;
        this.dateAdded = dateAdded;
        this.lastSuccess = lastSuccess;
        this.lastTry = lastTry;
        this.numberTry = numberTry;
        this.numberSuccess = numberSuccess;
        this.context = context;
        this.knowledgeLevel = knowledgeLevel;
        this.isFavorite = isFavorite;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setLastSuccess(Long lastSuccess) {
        this.lastSuccess = lastSuccess;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public Long getLastSuccess() {
        return lastSuccess;
    }

    public Long getLastTry() {
        return lastTry;
    }

    public void setLastTry(Long lastTry) {
        this.lastTry = lastTry;
    }

    public String getWordNative() {
        return wordNative;
    }

    public void setWordNative(String wordNative) {
        this.wordNative = wordNative;
    }

    public String getWordTranslated() {
        return wordTranslated;
    }

    public void setWordTranslated(String wordTranslated) {
        this.wordTranslated = wordTranslated;
    }

    public Integer getNumberTry() {
        return numberTry;
    }

    public void setNumberTry(Integer numberTry) {
        this.numberTry = numberTry;
    }

    public Integer getNumberSuccess() {
        return numberSuccess;
    }

    public void setNumberSuccess(Integer numberSuccess) {
        this.numberSuccess = numberSuccess;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public Integer getKnowledgeLevel() { return knowledgeLevel; }

    public void setKnowledgeLevel(Integer knowledgeLevel) { this.knowledgeLevel = knowledgeLevel; }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}