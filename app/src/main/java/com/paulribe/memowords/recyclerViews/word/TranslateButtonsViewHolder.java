package com.paulribe.memowords.recyclerViews.word;

import android.view.View;
import android.widget.ImageButton;
import com.paulribe.memowords.R;
import androidx.recyclerview.widget.RecyclerView;

public class TranslateButtonsViewHolder extends RecyclerView.ViewHolder {

    private View.OnClickListener ponsClickListener;
    private View.OnClickListener googleTranslateClickListener;
    private ImageButton buttonCallPons;
    private ImageButton buttonCallTranslate;

    public TranslateButtonsViewHolder(View itemView, View.OnClickListener ponsClickListener, View.OnClickListener googleTranslateClickListener) {
        super(itemView);
        buttonCallPons = itemView.findViewById(R.id.buttonCallPons);
        buttonCallTranslate = itemView.findViewById(R.id.buttonCallTranslate);
        this.ponsClickListener = ponsClickListener;
        this.googleTranslateClickListener = googleTranslateClickListener;
    }

    public void updateWithTranslateCallView() {
        buttonCallPons.setOnClickListener(ponsClickListener);
        buttonCallTranslate.setOnClickListener(googleTranslateClickListener);
    }
}
