package com.paulribe.memowords.common.recyclerviews.translationresult;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.recyclerviews.OnWordTranslatedClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationResultRowViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewWordSource;
    private TextView textViewWordTarget;
    private Button addResultWordButton;
    private OnWordTranslatedClickListener translatedWordListener;

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
