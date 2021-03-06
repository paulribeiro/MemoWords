package com.paulribe.memowords.recyclerViews.translationResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.R;
import com.paulribe.memowords.model.TranslatedWord;
import com.paulribe.memowords.recyclerViews.OnExpandSectionClickListener;
import com.paulribe.memowords.recyclerViews.OnWordTranslatedClickListener;
import com.paulribe.memowords.recyclerViews.word.NoContentViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String searchedWord;
    private List<TranslatedWord> possibleTranslations;
    private OnWordTranslatedClickListener onWordTranslatedClickListener;
    private OnExpandSectionClickListener onExpandSectionClickListener;

    public TranslationResultAdapter(List<TranslatedWord> translations, OnWordTranslatedClickListener onWordTranslatedClickListener,
                                    OnExpandSectionClickListener onExpandSectionClickListener, String searchedWord) {
        this.possibleTranslations = translations;
        this.onWordTranslatedClickListener = onWordTranslatedClickListener;
        this.onExpandSectionClickListener = onExpandSectionClickListener;
        this.searchedWord = searchedWord;
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
        } else if(viewType == 2){
            view = inflater.inflate(R.layout.word_translated_section_item, parent, false);
            return new TranslationResultSectionViewHolder(view, onExpandSectionClickListener);
        } else {
            view = inflater.inflate(R.layout.no_result_item, parent, false);
            return new NoContentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ((TranslationResultRowViewHolder)holder).updateRow(possibleTranslations.get(position));
                break;
            case 1:
                ((TranslationResultSubsectionViewHolder)holder).updateSubsection(possibleTranslations.get(position));
                break;
            case 2:
                ((TranslationResultSectionViewHolder)holder).updateSection(possibleTranslations.get(position));
                break;
            case 3:
                ((NoContentViewHolder)holder).updateNoContentItemWithImage(searchedWord, holder.itemView.getContext());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if(CollectionUtils.isEmpty(this.possibleTranslations)) {
            return 3;
        } else {
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
    }

    @Override
    public int getItemCount() {
        if(CollectionUtils.isEmpty(this.possibleTranslations)) {
            return 1;
        } else {
            return this.possibleTranslations.size();
        }
    }

    public List<TranslatedWord> getPossibleTranslations() {
        return possibleTranslations;
    }

    public void setPossibleTranslations(List<TranslatedWord> possibleTranslations) {
        this.possibleTranslations = possibleTranslations;
    }

    public String getSearchedWord() {
        return searchedWord;
    }

    public void setSearchedWord(String searchedWord) {
        this.searchedWord = searchedWord;
    }
}
