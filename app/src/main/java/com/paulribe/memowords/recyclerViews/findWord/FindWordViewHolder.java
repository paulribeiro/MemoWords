package com.paulribe.memowords.recyclerViews.findWord;

import android.view.View;
import android.widget.TextView;

import com.paulribe.memowords.R;

import androidx.recyclerview.widget.RecyclerView;

public class FindWordViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView textViewWordTitle;

    public FindWordViewHolder(View itemView) {
        super(itemView);
        textViewWordTitle = itemView.findViewById(R.id.textView_title);
        view = itemView.findViewById(R.id.item_title_background);
    }

    public void updateWithWordTitle(String title, Integer position) {
        if(position % 2 == 0) {
            view.setBackgroundColor(0xFFe0e0e0);
        } else {
            view.setBackgroundColor(0xFFFFFFFF);
        }
        this.textViewWordTitle.setText(title);
    }
}
