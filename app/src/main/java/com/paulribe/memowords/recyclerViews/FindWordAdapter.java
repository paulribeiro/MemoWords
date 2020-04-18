package com.paulribe.memowords.recyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulribe.memowords.R;
import com.paulribe.memowords.model.Word;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FindWordAdapter extends RecyclerView.Adapter<FindWordViewHolder> {

    // FOR DATA
    private List<Word> words;
    private boolean isLanguageFR;
    private OnViewClickListener listener;


    // CONSTRUCTOR
    public FindWordAdapter(List<Word> words, boolean isLanguageFR, OnViewClickListener listener) {
        this.words = words;
        this.isLanguageFR = isLanguageFR;
        this.listener = listener;
    }

    public interface OnViewClickListener {
        void onViewClick(Word word);
    }

    @Override
    public FindWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_title_item, parent, false);

        return new FindWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindWordViewHolder holder, int position) {
        if(isLanguageFR) {
            holder.updateWithWordTitle(this.words.get(position).getWordFR(), position);
        } else {
            holder.updateWithWordTitle(this.words.get(position).getWordDE(), position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewClick(words.get(position));
            }
        });
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