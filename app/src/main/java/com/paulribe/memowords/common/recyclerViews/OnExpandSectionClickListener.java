package com.paulribe.memowords.common.recyclerViews;

import android.view.View;

import com.paulribe.memowords.common.model.TranslatedWord;

public class OnExpandSectionClickListener implements View.OnClickListener {

    private TranslatedWord translatedSection;

    public OnExpandSectionClickListener() {
    }

    public OnExpandSectionClickListener(TranslatedWord translatedSection) {
        this.translatedSection = translatedSection;
    }

    @Override
    public void onClick(View v) {

    }

    public TranslatedWord getTranslatedSection() {
        return translatedSection;
    }

    public void setTranslatedSection(TranslatedWord translatedSection) {
        this.translatedSection = translatedSection;
    }
}