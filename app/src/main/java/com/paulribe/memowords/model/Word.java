package com.paulribe.memowords.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Word {

    private String wordId;
    private String wordFR;
    private String wordDE;
    private long dateAdded;
    private long lastSuccess;
    private long lastTry;
    private Integer numberTry;
    private Integer numberSuccess;
    private String context;
    private Integer knowledgeLevel;
    private boolean isFavorite;

    public Word() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Word(String wordFR, String wordDE, long dateAdded, long lastSuccess, long lastTry, Integer numberTry, Integer numberSuccess, String context, Integer knowledgeLevel, boolean isFavorite) {
        this.wordFR = wordFR;
        this.wordDE = wordDE;
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

    public void setLastSuccess(long lastSuccess) {
        this.lastSuccess = lastSuccess;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public long getLastSuccess() {
        return lastSuccess;
    }

    public long getLastTry() {
        return lastTry;
    }

    public void setLastTry(long lastTry) {
        this.lastTry = lastTry;
    }

    public String getWordFR() {
        return wordFR;
    }

    public void setWordFR(String wordFR) {
        this.wordFR = wordFR;
    }

    public String getWordDE() {
        return wordDE;
    }

    public void setWordDE(String wordDE) {
        this.wordDE = wordDE;
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