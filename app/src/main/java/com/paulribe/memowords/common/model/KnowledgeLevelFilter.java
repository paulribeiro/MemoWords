package com.paulribe.memowords.common.model;

import com.paulribe.memowords.common.enumeration.KnowledgeLevelEnum;

public class KnowledgeLevelFilter {

    private KnowledgeLevelEnum knowledgeLevelEnum;
    private Boolean isSelected;

    public KnowledgeLevelFilter(KnowledgeLevelEnum knowledgeLevelEnum, Boolean isSelected) {
        this.knowledgeLevelEnum = knowledgeLevelEnum;
        this.isSelected = isSelected;
    }

    public KnowledgeLevelEnum getKnowledgeLevelEnum() {
        return knowledgeLevelEnum;
    }

    public void setKnowledgeLevelEnum(KnowledgeLevelEnum knowledgeLevelEnum) {
        this.knowledgeLevelEnum = knowledgeLevelEnum;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
