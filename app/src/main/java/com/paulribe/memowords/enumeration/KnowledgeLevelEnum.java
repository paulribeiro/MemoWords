package com.paulribe.memowords.enumeration;

import android.content.Context;

import com.paulribe.memowords.R;

public enum KnowledgeLevelEnum {
    NEW(R.string.neww),
    SHORT_TERM_MEMORY(R.string.short_term_memory),
    MID_TERM_MEMORY(R.string.mid_term_memory),
    LONG_TERM_MEMORY(R.string.known);

    private int knowledgeLevel;

    KnowledgeLevelEnum(int knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public String getKnowledgeLevel(Context context) {
        return context.getResources().getString(knowledgeLevel);
    }
}
