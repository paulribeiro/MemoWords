package com.paulribe.memowords.common.recyclerviews.translationresult;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.TranslatedWord;
import com.paulribe.memowords.common.restclient.HtmlHelper;

public class TranslationResultSubsectionViewHolder extends RecyclerView.ViewHolder {

    private final View view;
    private final TextView textViewSubsectionTitle;

    public TranslationResultSubsectionViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.item_title_subsection_view);
        textViewSubsectionTitle = itemView.findViewById(R.id.textView_subsection);
    }

    public void updateSubsection(TranslatedWord translatedWordRow) {
        if(Boolean.TRUE.equals(translatedWordRow.getHidden())) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            textViewSubsectionTitle.setText(HtmlHelper.html2text(translatedWordRow.getSourceWord()));
        }
    }
}
