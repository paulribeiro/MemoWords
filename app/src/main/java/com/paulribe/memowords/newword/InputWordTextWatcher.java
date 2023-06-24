package com.paulribe.memowords.newword;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.common.recyclerviews.findword.FindWordAdapter;

import java.util.stream.Collectors;

public class InputWordTextWatcher implements TextWatcher {

    private final NewWordViewModel newWordViewModel;
    private final RecyclerView suggestionWordRecyclerView;
    private final FindWordAdapter adapterWord;
    private final Button addButton;
    private final EditText inputWordNative;
    private final EditText inputWordTranslation;

    public InputWordTextWatcher(NewWordFragment newWordFragment, boolean isNativeInputWord) {
        this.newWordViewModel = newWordFragment.getNewWordViewModel();
        this.addButton = newWordFragment.getAddButton();
        this.inputWordNative = newWordFragment.getInputWordNative();
        this.inputWordTranslation = newWordFragment.getInputWordTranslation();
        if(isNativeInputWord) {
            this.suggestionWordRecyclerView = newWordFragment.getSuggestionWordNativeRecyclerView();
            this.adapterWord = newWordFragment.getAdapterWordNative();
        } else {
            this.suggestionWordRecyclerView = newWordFragment.getSuggestionWordTranslationRecyclerView();
            this.adapterWord = newWordFragment.getAdapterWordTranslation();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No action to perform there.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(newWordViewModel.getNewWord() == null) {
            if(s.toString().isEmpty() || CollectionUtils.isEmpty(newWordViewModel.getWords()) || s.toString().length() < 3) {
                suggestionWordRecyclerView.setVisibility(View.GONE);
            } else {
                suggestionWordRecyclerView.setVisibility(View.VISIBLE);
                adapterWord.setWords(newWordViewModel.getWords().stream().filter(w -> w.getWordTranslated().toLowerCase().contains(s.toString().toLowerCase()))
                        .collect(Collectors.toList()));
                adapterWord.notifyDataSetChanged();
            }
        }
        checkRequiredFields();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // No action to perform there.
    }

    private void checkRequiredFields() {
        addButton.setEnabled(!inputWordNative.getText().toString().isEmpty() && !inputWordTranslation.getText().toString().isEmpty());
    }
}
