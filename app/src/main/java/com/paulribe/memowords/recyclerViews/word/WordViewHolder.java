package com.paulribe.memowords.recyclerViews.word;

import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.paulribe.memowords.R;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.recyclerViews.OnFavoriteClickListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class WordViewHolder  extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewWord;
    private TextView textViewTranslation;
    private TextView textViewLastTry;
    private ImageButton favoriteButton;
    private OnFavoriteClickListener favoriteCLickListener;
    private View knowledgeLevelView;
    private ProgressBar progressBarKnowledgeLevel;

    public WordViewHolder(View itemView, OnFavoriteClickListener favoriteCLickListener) {
        super(itemView);
        textViewWord = itemView.findViewById(R.id.item_word);
        textViewTranslation = itemView.findViewById(R.id.item_word_translation);
        textViewLastTry = itemView.findViewById(R.id.item_last_try);
        view = itemView.findViewById(R.id.item_view);
        favoriteButton = itemView.findViewById(R.id.favorite);
        knowledgeLevelView = itemView.findViewById(R.id.knowledgeLevelView);
        progressBarKnowledgeLevel = itemView.findViewById(R.id.progressBarKnowledgeLevel);
        this.favoriteCLickListener = favoriteCLickListener;
    }

    public void updateWithWord(Word word, Integer position, Boolean isNativeLanguageToTranslation) {

        view.setBackgroundColor(0xFFFFFFFF);
        if(isNativeLanguageToTranslation) {
            this.textViewWord.setText(word.getWordFR());
            this.textViewTranslation.setText(word.getWordDE());
        } else {
            this.textViewWord.setText(word.getWordDE());
            this.textViewTranslation.setText(word.getWordFR());
        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date(word.getLastTry());
        if(d.after(new Date(920000000))) {
            textViewLastTry.setVisibility(View.VISIBLE);
            textViewLastTry.setText("Last try : " + dateFormat.format(d));
        }
        else {
            textViewLastTry.setVisibility(View.GONE);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word.setFavorite(!word.isFavorite());
                updateFavoriteButton(word);
                favoriteCLickListener.setWord(word);
                favoriteCLickListener.onClick(view);
            }
        });
        switch(word.getKnowledgeLevel()) {
            case 0:
                knowledgeLevelView.setBackgroundResource(R.color.red);
                progressBarKnowledgeLevel.setProgress(5);
                progressBarKnowledgeLevel.getProgressDrawable().setColorFilter(this.itemView.getContext().getResources().getColor(R.color.red),
                        android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case 1:
                knowledgeLevelView.setBackgroundResource(R.color.orange);
                progressBarKnowledgeLevel.setProgress(20);
                progressBarKnowledgeLevel.getProgressDrawable().setColorFilter(this.itemView.getContext().getResources().getColor(R.color.orange),
                        android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case 2:
                knowledgeLevelView.setBackgroundResource(R.color.yellow);
                progressBarKnowledgeLevel.setProgress(40);
                progressBarKnowledgeLevel.getProgressDrawable().setColorFilter(this.itemView.getContext().getResources().getColor(R.color.yellow),
                        android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case 3:
                knowledgeLevelView.setBackgroundResource(R.color.clearGreen);
                progressBarKnowledgeLevel.setProgress(60);
                progressBarKnowledgeLevel.getProgressDrawable().setColorFilter(this.itemView.getContext().getResources().getColor(R.color.clearGreen),
                        android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case 4:
                knowledgeLevelView.setBackgroundResource(R.color.clearGreen);
                progressBarKnowledgeLevel.setProgress(80);
                break;
            case 5:
                knowledgeLevelView.setBackgroundResource(R.color.green);
                progressBarKnowledgeLevel.setProgress(100);
                break;
        }
        updateFavoriteButton(word);
    }

    private void updateFavoriteButton(Word word) {
        if(word.isFavorite()) {
            favoriteButton.setImageResource(R.drawable.star_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.star_empty);
        }
    }
}
