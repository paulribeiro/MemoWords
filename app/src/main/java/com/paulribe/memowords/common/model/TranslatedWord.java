package com.paulribe.memowords.common.model;

import com.paulribe.memowords.common.enumeration.SectionRowEnum;

public class TranslatedWord {

    private SectionRowEnum sectionRowtype;
    private String sourceWord;
    private String targetWord;
    private String wordClass;
    private Integer sectionNumber;
    private Integer subSectionNumber;
    private Boolean isHidden;

    public TranslatedWord(SectionRowEnum sectionRowtype, String sourceWord, String targetWord, String wordClass, Integer sectionNumber, Integer subSectionNumber, Boolean isHidden) {
        this.sectionRowtype = sectionRowtype;
        this.sourceWord = sourceWord;
        this.targetWord = targetWord;
        this.wordClass = wordClass;
        this.sectionNumber = sectionNumber;
        this.subSectionNumber = subSectionNumber;
        this.isHidden = isHidden;
    }

    public TranslatedWord(SectionRowEnum sectionRowtype, String sourceWord, String wordClass, Integer sectionNumber, Boolean isHidden) {
        this.sectionRowtype = sectionRowtype;
        this.sourceWord = sourceWord;
        this.wordClass = wordClass;
        this.sectionNumber = sectionNumber;
        this.isHidden = isHidden;
    }

    public TranslatedWord(SectionRowEnum sectionRowtype, String sourceWord, Integer sectionNumber, Integer subSectionNumber, Boolean isHidden) {
        this.sectionRowtype = sectionRowtype;
        this.sourceWord = sourceWord;
        this.sectionNumber = sectionNumber;
        this.subSectionNumber = subSectionNumber;
        this.isHidden = isHidden;
    }

    public TranslatedWord(SectionRowEnum sectionRowtype, String sourceWord, String targetWord, Integer sectionNumber, Integer subSectionNumber, Boolean isHidden) {
        this.sectionRowtype = sectionRowtype;
        this.sourceWord = sourceWord;
        this.targetWord = targetWord;
        this.sectionNumber = sectionNumber;
        this.subSectionNumber = subSectionNumber;
        this.isHidden = isHidden;
    }

    public SectionRowEnum getSectionRowtype() {
        return sectionRowtype;
    }

    public void setSectionRowtype(SectionRowEnum sectionRowtype) {
        this.sectionRowtype = sectionRowtype;
    }

    public String getSourceWord() {
        return sourceWord;
    }

    public void setSourceWord(String sourceWord) {
        this.sourceWord = sourceWord;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public String getWordClass() {
        return wordClass;
    }

    public void setWordClass(String wordClass) {
        this.wordClass = wordClass;
    }

    public Integer getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(Integer sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public Integer getSubSectionNumber() {
        return subSectionNumber;
    }

    public void setSubSectionNumber(Integer subSectionNumber) {
        this.subSectionNumber = subSectionNumber;
    }
}
