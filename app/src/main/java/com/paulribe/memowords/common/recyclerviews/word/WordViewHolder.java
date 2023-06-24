package com.paulribe.memowords.common.recyclerviews.word;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.common.recyclerviews.OnFavoriteClickListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WordViewHolder  extends RecyclerView.ViewHolder {

    private final View view;
    private final TextView textViewWord;
    private final TextView textViewTranslation;
    private final TextView textViewLastTry;
    private final ImageButton favoriteButton;
    private final OnFavoriteClickListener favoriteCLickListener;
    private final View knowledgeLevelView;
    private final ProgressBar progressBarKnowledgeLevel;

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

    public void updateWithWord(Word word, Boolean isNativeLanguageToTranslation, Context context) {

        view.setBackgroundColor(0xFFFFFFFF);
        if(isNativeLanguageToTranslation) {
            this.textViewWord.setText(word.getWordNative());
            this.textViewTranslation.setText(word.getWordTranslated());
        } else {
            this.textViewWord.setText(word.getWordTranslated());
            this.textViewTranslation.setText(word.getWordNative());
        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if(word.getLastTry() != null) {
            Date d = new Date(word.getLastTry());
            textViewLastTry.setVisibility(View.VISIBLE);
            textViewLastTry.setText(String.format("%s %s", context.getResources().getString(R.string.last_try), dateFormat.format(d)));
        }
        else {
            textViewLastTry.setVisibility(View.GONE);
        }

        favoriteButton.setOnClickListener(view -> {
            word.setFavorite(!word.isFavorite());
            updateFavoriteButton(word);
            favoriteCLickListener.setWord(word);
            favoriteCLickListener.onClick(view);
        });

        switch(word.getKnowledgeLevel()) {
            case 0:
                setProgressCircle(R.color.red, 5);
                break;
            case 1:
                setProgressCircle(R.color.orange, 20);
                break;
            case 2:
                setProgressCircle(R.color.orange, 40);
                break;
            case 3:
                setProgressCircle(R.color.yellow, 60);
                break;
            case 4:
                setProgressCircle(R.color.yellow, 80);
                break;
            default:
                setProgressCircle(R.color.green, 100);
                break;
        }
        updateFavoriteButton(word);
    }

    private void setProgressCircle(int color, int progress) {
        knowledgeLevelView.setBackgroundResource(color);
        progressBarKnowledgeLevel.setProgress(progress);
        progressBarKnowledgeLevel.getProgressDrawable().setColorFilter(
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        ContextCompat.getColor(this.itemView.getContext(), color),
                        BlendModeCompat.SRC_ATOP));
    }

    private void updateFavoriteButton(Word word) {
        if(word.isFavorite()) {
            favoriteButton.setImageResource(R.drawable.star_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.star_empty);
        }
    }
}
