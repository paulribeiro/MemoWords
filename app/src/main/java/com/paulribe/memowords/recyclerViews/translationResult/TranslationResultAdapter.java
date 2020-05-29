package com.paulribe.memowords.recyclerViews.translationResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.paulribe.memowords.R;
import com.paulribe.memowords.model.TranslatedWord;
import com.paulribe.memowords.recyclerViews.OnExpandSectionClickListener;
import com.paulribe.memowords.recyclerViews.OnWordTranslatedClickListener;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TranslatedWord> possibleTranslations;
    private OnWordTranslatedClickListener onWordTranslatedClickListener;
    private OnExpandSectionClickListener onExpandSectionClickListener;

    public TranslationResultAdapter(List<TranslatedWord> translations, OnWordTranslatedClickListener onWordTranslatedClickListener,
                                    OnExpandSectionClickListener onExpandSectionClickListener) {
        this.possibleTranslations = translations;
        this.onWordTranslatedClickListener = onWordTranslatedClickListener;
        this.onExpandSectionClickListener = onExpandSectionClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == 0) {
            view = inflater.inflate(R.layout.word_translated_item, parent, false);
            return new TranslationResultRowViewHolder(view, onWordTranslatedClickListener);
        } else if(viewType == 1){
            view = inflater.inflate(R.layout.word_translated_subsection_item, parent, false);
            return new TranslationResultSubsectionViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.word_translated_section_item, parent, false);
            return new TranslationResultSectionViewHolder(view, onExpandSectionClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TranslatedWord translatedWord = possibleTranslations.get(position);
        switch (translatedWord.getSectionRowtype()) {
            case ROW:
                ((TranslationResultRowViewHolder)holder).updateRow(translatedWord);
                break;
            case SUBSECTION:
                ((TranslationResultSubsectionViewHolder)holder).updateSubsection(translatedWord);
                break;
            case SECTION:
                ((TranslationResultSectionViewHolder)holder).updateSection(translatedWord);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        TranslatedWord translatedWord = possibleTranslations.get(position);
        switch(translatedWord.getSectionRowtype()) {
            case ROW:
                return 0;
            case SUBSECTION:
                return 1;
            case SECTION:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return this.possibleTranslations.size();
    }

    public List<TranslatedWord> getPossibleTranslations() {
        return possibleTranslations;
    }

    public void setPossibleTranslations(List<TranslatedWord> possibleTranslations) {
        this.possibleTranslations = possibleTranslations;
    }
}
