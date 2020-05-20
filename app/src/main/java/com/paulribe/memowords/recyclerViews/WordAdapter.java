package com.paulribe.memowords.recyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulribe.memowords.R;
import com.paulribe.memowords.model.Word;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class WordAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private List<Word> words;

    private OnFavoriteClickListener favoriteCLickListener;

    public WordAdapter(List<Word> words, OnFavoriteClickListener listener) {
        this.words = words;
        favoriteCLickListener = listener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_item, parent, false);

        return new WordViewHolder(view, favoriteCLickListener);
    }

    @Override
    public void onBindViewHolder(WordViewHolder viewHolder, int position) {
        viewHolder.updateWithWord(this.words.get(position), position);

    }

    @Override
    public int getItemCount() {
        return this.words.size();
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}