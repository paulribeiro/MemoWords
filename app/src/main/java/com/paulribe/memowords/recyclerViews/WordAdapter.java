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

    // FOR DATA
    private List<Word> words;

    // CONSTRUCTOR
    public WordAdapter(List<Word> words) {
        this.words = words;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_item, parent, false);

        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder viewHolder, int position) {
        viewHolder.updateWithWord(this.words.get(position), position);

    }

    @Override
    public int getItemCount() {
        return this.words.size();
    }
}