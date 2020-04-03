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
    private Integer numberTry;
    private Integer numberSuccess;
    private String context;

    public Word() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Word(String wordFR, String wordDE, Date dateAdded, Date lastSuccess, Integer numberTry, Integer numberSuccess, String context) {
        this.wordFR = wordFR;
        this.wordDE = wordDE;
        this.dateAdded = dateAdded;
        this.lastSuccess = lastSuccess;
        this.numberTry = numberTry;
        this.numberSuccess = numberSuccess;
        this.context = context;
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
}