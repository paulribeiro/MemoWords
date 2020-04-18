package com.paulribe.memowords.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Word {

    private Integer id;
    private String wordFR;
    private String wordDE;
    private long dateAdded;
    private long lastSuccess;
    private long lastTry;
    private Integer numberTry;
    private Integer numberSuccess;
    private String context;
    private Integer knowledgeLevel;

    public Word() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Word(String wordFR, String wordDE, long dateAdded, long lastSuccess, long lastTry, Integer numberTry, Integer numberSuccess, String context, Integer knowledgeLevel) {
        this.wordFR = wordFR;
        this.wordDE = wordDE;
        this.dateAdded = dateAdded;
        this.lastSuccess = lastSuccess;
        this.lastTry = lastTry;
        this.numberTry = numberTry;
        this.numberSuccess = numberSuccess;
        this.context = context;
        this.knowledgeLevel = knowledgeLevel;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKnowledgeLevel() { return knowledgeLevel; }

    public void setKnowledgeLevel(Integer knowledgeLevel) { this.knowledgeLevel = knowledgeLevel; }
}