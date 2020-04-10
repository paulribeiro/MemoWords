package com.paulribe.memowords.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Word {

    private Integer id;
    private String wordFR;
    private String wordDE;
    private Date dateAdded;
    private Date lastSuccess;
    private Date lastTry;
    private Integer numberTry;
    private Integer numberSuccess;
    private String context;
    private Integer knowledgeLevel;

    public Word() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Word(Integer id, String wordFR, String wordDE, Date dateAdded, Date lastSuccess,
                Date lastTry, Integer numberTry, Integer numberSuccess, String context, Integer knowledgeLevel) {
        this.id = id;
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

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getLastSuccess() {
        return lastSuccess;
    }

    public void setLastSuccess(Date lastSuccess) {
        this.lastSuccess = lastSuccess;
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

    public Date getLastTry() {
        return lastTry;
    }

    public void setLastTry(Date lastTry) {
        this.lastTry = lastTry;
    }

    public Integer getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(Integer knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }
}