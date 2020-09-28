package com.paulribe.memowords.recyclerViews.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.paulribe.memowords.R;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.recyclerViews.OnFavoriteClickListener;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class WordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Word> words;
    private Boolean isNativeLanguageToTranslation;
    private OnFavoriteClickListener favoriteCLickListener;
    private View.OnClickListener ponsClickListener;
    private View.OnClickListener mymemoryClickListener;

    public WordAdapter(List<Word> words, OnFavoriteClickListener listener, View.OnClickListener ponsClickListener, View.OnClickListener mymemoryClickListener) {
        this.words = words;
        this.favoriteCLickListener = listener;
        this.ponsClickListener = ponsClickListener;
        this.mymemoryClickListener = mymemoryClickListener;
        this.isNativeLanguageToTranslation = Boolean.TRUE;
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
            return new TranslateButtonsViewHolder(view, ponsClickListener, mymemoryClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(position != words.size()) {
            ((WordViewHolder)viewHolder).updateWithWord(this.words.get(position), position, isNativeLanguageToTranslation, viewHolder.itemView.getContext());
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

    public Boolean getNativeLanguageToTranslation() {
        return isNativeLanguageToTranslation;
    }

    public void setNativeLanguageToTranslation(Boolean nativeLanguageToTranslation) {
        isNativeLanguageToTranslation = nativeLanguageToTranslation;
    }
}