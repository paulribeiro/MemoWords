package com.paulribe.memowords.recyclerViews.word;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.paulribe.memowords.R;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.recyclerViews.OnFavoriteClickListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class WordViewHolder  extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewWord;
    private TextView textViewLastTryText;
    private TextView textViewTranslation;
    private TextView textViewLastTry;
    private ImageButton favoriteButton;
    private OnFavoriteClickListener favoriteCLickListener;


    public WordViewHolder(View itemView, OnFavoriteClickListener favoriteCLickListener) {
        super(itemView);
        textViewWord = itemView.findViewById(R.id.item_word);
        textViewTranslation = itemView.findViewById(R.id.item_word_translation);
        textViewLastTry = itemView.findViewById(R.id.item_last_try);
        textViewLastTryText = itemView.findViewById(R.id.textViewLastTry);
        view = itemView.findViewById(R.id.item_view);
        favoriteButton = itemView.findViewById(R.id.favorite);
        this.favoriteCLickListener = favoriteCLickListener;
    }

    public void updateWithWord(Word word, Integer position) {
        if(position % 2 == 0) {
            view.setBackgroundColor(0xFFe0e0e0);
        } else {
            view.setBackgroundColor(0xFFFFFFFF);
        }
        this.textViewWord.setText(word.getWordFR());
        this.textViewTranslation.setText(word.getWordDE());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date(word.getLastTry());
        if(d.after(new Date(920000000))) {
            textViewLastTryText.setVisibility(View.VISIBLE);
            textViewLastTry.setVisibility(View.VISIBLE);
            textViewLastTry.setText(dateFormat.format(d));
        }
        else {
            textViewLastTryText.setVisibility(View.GONE);
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
