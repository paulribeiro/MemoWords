package com.paulribe.memowords.common.recyclerviews.findword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.Word;

import java.util.List;

public class FindWordAdapter extends RecyclerView.Adapter<FindWordViewHolder> {

    private List<Word> words;
    private final boolean isLanguageFR;
    private final OnViewClickListener listener;

    public FindWordAdapter(List<Word> words, boolean isLanguageFR, OnViewClickListener listener) {
        this.words = words;
        this.isLanguageFR = isLanguageFR;
        this.listener = listener;
    }

    public interface OnViewClickListener {
        void onViewClick(Word word);
    }

    @NonNull
    @Override
    public FindWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_title_item, parent, false);

        return new FindWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindWordViewHolder holder, int position) {
        if(isLanguageFR) {
            holder.updateWithWordTitle(this.words.get(position).getWordNative(), position);
        } else {
            holder.updateWithWordTitle(this.words.get(position).getWordTranslated(), position);
        }
        holder.itemView.setOnClickListener(v -> listener.onViewClick(words.get(position)));
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