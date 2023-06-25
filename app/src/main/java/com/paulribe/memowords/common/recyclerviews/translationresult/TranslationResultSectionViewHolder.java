package com.paulribe.memowords.common.recyclerviews.translationresult;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.recyclerviews.OnExpandSectionClickListener;
import com.paulribe.memowords.common.restclient.HtmlHelper;

import java.util.Objects;

public class TranslationResultSectionViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewSectionTitle;
    private final ImageView expandImageView;
    private final OnExpandSectionClickListener onExpandSectionClickListener;

    public TranslationResultSectionViewHolder(@NonNull View itemView, OnExpandSectionClickListener onExpandSectionClickListener) {
        super(itemView);
        textViewSectionTitle = itemView.findViewById(R.id.textView_section);
        expandImageView = itemView.findViewById(R.id.expandImageView);
        this.onExpandSectionClickListener = onExpandSectionClickListener;
    }

    public void updateSection(TranslatedWord translatedWordSection) {
        if(Boolean.TRUE.equals(translatedWordSection.getHidden())) {
            expandImageView.setRotation(180);
        } else {
            expandImageView.setRotation(0);
        }
        textViewSectionTitle.setText(HtmlHelper.html2text(
                String.format("%s %s",translatedWordSection.getSourceWord(),
                        Objects.toString(translatedWordSection.getWordClass(), ""))));
        expandImageView.setOnClickListener(view1 -> {
            translatedWordSection.setHidden(!translatedWordSection.getHidden());
            onExpandSectionClickListener.setTranslatedSection(translatedWordSection);
            onExpandSectionClickListener.onClick(view1);
        });
    }
}
