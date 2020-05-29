package com.paulribe.memowords.recyclerViews.translationResult;

import android.view.View;
import android.widget.TextView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.model.TranslatedWord;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationResultSubsectionViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewSubsectionTitle;

    public TranslationResultSubsectionViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.item_title_subsection_view);
        textViewSubsectionTitle = itemView.findViewById(R.id.textView_subsection);
    }

    public void updateSubsection(TranslatedWord translatedWordRow) {
        if(translatedWordRow.getHidden()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            textViewSubsectionTitle.setText(html2text(translatedWordRow.getSourceWord()));
        }
    }

    public static String html2text(String html) {
        return android.text.Html.fromHtml(html).toString();
    }
}
