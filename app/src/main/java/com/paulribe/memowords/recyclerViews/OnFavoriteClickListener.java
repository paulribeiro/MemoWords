package com.paulribe.memowords.recyclerViews;

import android.view.View;

import com.paulribe.memowords.model.Word;

public class OnFavoriteClickListener implements View.OnClickListener {

    private Word word;

    public OnFavoriteClickListener() {
    }

    public OnFavoriteClickListener(Word word) {
        this.word = word;
    }

    @Override
    public void onClick(View v) {

    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
};
