package com.paulribe.memowords.enumeration;

public enum KnowledgeLevelEnum {
    NEW("New"),
    SHORT_TERM_MEMORY("Short term memory"),
    MID_TERM_MEMORY("Mid term memory"),
    LONG_TERM_MEMORY("Known");

    private String knowledgeLevel;

    KnowledgeLevelEnum(String knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public String getKnowledgeLevel() {
        return knowledgeLevel;
    }
}
