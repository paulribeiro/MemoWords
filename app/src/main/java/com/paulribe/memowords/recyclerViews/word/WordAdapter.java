package com.paulribe.memowords.recyclerViews.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulribe.memowords.R;
import com.paulribe.memowords.model.TranslatedWord;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.recyclerViews.OnFavoriteClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private List<Word> words;

    private OnFavoriteClickListener favoriteCLickListener;
    private View.OnClickListener ponsClickListener;
    private View.OnClickListener googleTranslateClickListener;

    public WordAdapter(List<Word> words, OnFavoriteClickListener listener, View.OnClickListener ponsClickListener, View.OnClickListener googleTranslateClickListener) {
        this.words = words;
        this.favoriteCLickListener = listener;
        this.ponsClickListener = ponsClickListener;
        this.googleTranslateClickListener = googleTranslateClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == 0) {
            View view = inflater.inflate(R.layout.word_item, parent, false);
            return new WordViewHolder(view, favoriteCLickListener);
        } else {
            View view = inflater.inflate(R.layout.translate_word_buttons_item, parent, false);
            return new TranslateButtonsViewHolder(view, ponsClickListener, googleTranslateClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(position != words.size()) {
            ((WordViewHolder)viewHolder).updateWithWord(this.words.get(position), position);
        } else {
            ((TranslateButtonsViewHolder)viewHolder).updateWithTranslateCallView();
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if(position != words.size()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return this.words.size() + 1;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}