package com.paulribe.memowords.common.model;

import com.paulribe.memowords.common.enumeration.KnowledgeLevelEnum;

public class KnowledgeLevelFilter {

    private KnowledgeLevelEnum knowledgeLevelEnum;
    private boolean isSelected;

    public KnowledgeLevelFilter(KnowledgeLevelEnum knowledgeLevelEnum, boolean isSelected) {
        this.knowledgeLevelEnum = knowledgeLevelEnum;
        this.isSelected = isSelected;
    }

    public KnowledgeLevelEnum getKnowledgeLevelEnum() {
        return knowledgeLevelEnum;
    }

    public void setKnowledgeLevelEnum(KnowledgeLevelEnum knowledgeLevelEnum) {
        this.knowledgeLevelEnum = knowledgeLevelEnum;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
