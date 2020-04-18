package com.paulribe.memowords.recyclerViews;

import android.view.View;
import android.widget.TextView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.model.Word;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

public class WordViewHolder  extends RecyclerView.ViewHolder {

    View view;
    TextView textViewWord;
    TextView textViewTranslation;
    TextView textViewLastTry;

    public WordViewHolder(View itemView) {
        super(itemView);
        textViewWord = itemView.findViewById(R.id.item_word);
        textViewTranslation = itemView.findViewById(R.id.item_word_translation);
        textViewLastTry = itemView.findViewById(R.id.item_last_try);
        view = itemView.findViewById(R.id.item_view);
    }

    public void updateWithWord(Word word, Integer position) {
        if(position % 2 == 0) {
            view.setBackgroundColor(0xFFe0e0e0);
        } else {
            view.setBackgroundColor(0xFFFFFFFF);
        }
        this.textViewWord.setText(word.getWordFR());
        this.textViewTranslation.setText(word.getWordDE());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date(word.getLastTry()*1000);
        if(d.after(new Date(2020, 1, 1))) {
            this.textViewLastTry.setText(formatter.format(d));
        }
        else {
            this.textViewLastTry.setVisibility(View.GONE);
        }

    }
}
