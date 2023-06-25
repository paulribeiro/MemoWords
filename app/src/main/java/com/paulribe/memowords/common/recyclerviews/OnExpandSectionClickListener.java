package com.paulribe.memowords.common.recyclerviews;

import android.view.View;

import com.paulribe.memowords.common.model.TranslatedWord;

public class OnExpandSectionClickListener implements View.OnClickListener {

    private TranslatedWord translatedSection;

    public OnExpandSectionClickListener() {
        // No action to perform.
    }

    @Override
    public void onClick(View v) {
        // No action to perform.
    }

    public TranslatedWord getTranslatedSection() {
        return translatedSection;
    }

    public void setTranslatedSection(TranslatedWord translatedSection) {
        this.translatedSection = translatedSection;
    }
}