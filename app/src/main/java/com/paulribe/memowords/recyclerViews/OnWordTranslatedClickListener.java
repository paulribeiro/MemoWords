package com.paulribe.memowords.recyclerViews;

import android.view.View;
import com.paulribe.memowords.model.TranslatedWord;

public class OnWordTranslatedClickListener implements View.OnClickListener {

    private TranslatedWord translatedWord;

    public OnWordTranslatedClickListener() {
    }

    public OnWordTranslatedClickListener(TranslatedWord translatedWord) {
        this.translatedWord = translatedWord;
    }

    @Override
    public void onClick(View v) {

    }

    public TranslatedWord getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(TranslatedWord translatedWord) {
        this.translatedWord = translatedWord;
    }
}