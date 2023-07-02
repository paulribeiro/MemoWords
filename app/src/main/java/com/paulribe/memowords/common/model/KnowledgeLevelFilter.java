package com.paulribe.memowords.common.model;

import com.paulribe.memowords.common.enumeration.KnowledgeLevelEnum;

public class KnowledgeLevelFilter {

    private final KnowledgeLevelEnum knowledgeLevelEnum;
    private boolean isSelected;

    private final long wordsCountInKnowledgeLevel;

    public KnowledgeLevelFilter(KnowledgeLevelEnum knowledgeLevelEnum, boolean isSelected, long wordsCount) {
        this.knowledgeLevelEnum = knowledgeLevelEnum;
        this.isSelected = isSelected;
        this.wordsCountInKnowledgeLevel = wordsCount;
    }

    public KnowledgeLevelEnum getKnowledgeLevelEnum() {
        return knowledgeLevelEnum;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getWordsCountInKnowledgeLevel() {
        return wordsCountInKnowledgeLevel;
    }
}
