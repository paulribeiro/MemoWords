package com.paulribe.memowords.common.recyclerviews.translationresult;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.recyclerviews.OnWordTranslatedClickListener;

public class TranslationResultRowViewHolder extends RecyclerView.ViewHolder {

    private final View view;
    private final TextView textViewWordSource;
    private final TextView textViewWordTarget;
    private final Button addResultWordButton;
    private final OnWordTranslatedClickListener translatedWordListener;

    public TranslationResultRowViewHolder(@NonNull View itemView, OnWordTranslatedClickListener listener) {
        super(itemView);
        view = itemView.findViewById(R.id.item_title_row_view);
        textViewWordSource = itemView.findViewById(R.id.textView_source);
        textViewWordTarget = itemView.findViewById(R.id.textView_target);
        addResultWordButton = itemView.findViewById(R.id.addSearchedWordButton);
        translatedWordListener = listener;
    }

    public void updateRow(TranslatedWord translatedWordRow) {
        if(translatedWordRow.getHidden()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            textViewWordSource.setText(translatedWordRow.getSourceWord());
            textViewWordTarget.setText(translatedWordRow.getTargetWord());
            addResultWordButton.setOnClickListener(view -> {
                translatedWordListener.setTranslatedWord(translatedWordRow);
                translatedWordListener.onClick(view);
            });
        }
    }


}
