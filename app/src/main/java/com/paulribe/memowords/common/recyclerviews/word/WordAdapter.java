package com.paulribe.memowords.common.recyclerviews.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.recyclerviews.OnFavoriteClickListener;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Word> words;
    private Boolean isNativeLanguageToTranslation;
    private final OnFavoriteClickListener favoriteCLickListener;
    private final View.OnClickListener ponsClickListener;
    private final View.OnClickListener myMemoryClickListener;
    private String searchedWord;

    public WordAdapter(List<Word> words, OnFavoriteClickListener listener, View.OnClickListener ponsClickListener, View.OnClickListener myMemoryClickListener, String searchedWord) {
        this.words = words;
        this.favoriteCLickListener = listener;
        this.ponsClickListener = ponsClickListener;
        this.myMemoryClickListener = myMemoryClickListener;
        this.isNativeLanguageToTranslation = Boolean.TRUE;
        this.searchedWord = searchedWord;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == 0) {
            View view = inflater.inflate(R.layout.word_item, parent, false);
            return new WordViewHolder(view, favoriteCLickListener);
        } else if(viewType == 1) {
            View view = inflater.inflate(R.layout.no_result_item, parent, false);
            return new NoContentViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.translate_word_buttons_item, parent, false);
            return new TranslateButtonsViewHolder(view, ponsClickListener, myMemoryClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case 0:
                ((WordViewHolder)viewHolder).updateWithWord(this.words.get(position), isNativeLanguageToTranslation, viewHolder.itemView.getContext());
                break;
            case 1:
                ((NoContentViewHolder)viewHolder).updateNoContentItemWithoutImage();
                break;
            case 2:
                ((TranslateButtonsViewHolder)viewHolder).updateWithTranslateCallView(searchedWord, viewHolder.itemView.getContext());
                break;
            default:
                // not possible.
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if(words.isEmpty()) {
            if(position == 0) {
                return 1;
            } else {
                return 2;
            }
        } else {
            if(position != words.size()) {
                return 0;
            } else {
                return 2;
            }
        }
    }

    @Override
    public int getItemCount() {
        int wordsAndTranslationButtonItemsNumber = this.words.size() + (!searchedWord.equals("") ? 1 : 0);
        if(this.words.isEmpty()) {
            return wordsAndTranslationButtonItemsNumber + 1;
        } else {
            return wordsAndTranslationButtonItemsNumber;
        }
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public void setNativeLanguageToTranslation(Boolean nativeLanguageToTranslation) {
        isNativeLanguageToTranslation = nativeLanguageToTranslation;
    }

    public void setSearchedWord(String searchedWord) {
        this.searchedWord = searchedWord;
    }
}