package com.paulribe.memowords.common.recyclerviews.word;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.paulribe.memowords.R;

import androidx.recyclerview.widget.RecyclerView;

public class TranslateButtonsViewHolder extends RecyclerView.ViewHolder {

    private View.OnClickListener ponsClickListener;
    private View.OnClickListener googleTranslateClickListener;
    private ImageButton buttonCallPons;
    private ImageButton buttonCallTranslate;
    private TextView textViewTranslate;

    public TranslateButtonsViewHolder(View itemView, View.OnClickListener ponsClickListener, View.OnClickListener mymemoryClickListener) {
        super(itemView);
        buttonCallPons = itemView.findViewById(R.id.buttonCallPons);
        buttonCallTranslate = itemView.findViewById(R.id.buttonCallMyMemory);
        textViewTranslate = itemView.findViewById(R.id.TranslateWithTextView);
        if(ponsClickListener == null) {
            buttonCallPons.setVisibility(View.GONE);
        }
        this.ponsClickListener = ponsClickListener;
        this.googleTranslateClickListener = mymemoryClickListener;
    }

    public void updateWithTranslateCallView(String searchedWord, Context context) {
        buttonCallPons.setOnClickListener(ponsClickListener);
        buttonCallTranslate.setOnClickListener(googleTranslateClickListener);
        String translateWith = context.getResources().getString(R.string.translate_with, searchedWord);
        textViewTranslate.setText(translateWith);
    }
}
