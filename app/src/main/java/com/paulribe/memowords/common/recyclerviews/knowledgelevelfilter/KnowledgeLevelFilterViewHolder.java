package com.paulribe.memowords.common.recyclerviews.knowledgelevelfilter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.KnowledgeLevelFilter;

public class KnowledgeLevelFilterViewHolder extends RecyclerView.ViewHolder {

    private final View itemTitleView;
    private final TextView textViewTitle;


    public KnowledgeLevelFilterViewHolder(View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textView_title);
        itemTitleView = itemView.findViewById(R.id.item_filter_view);
    }

    public void update(KnowledgeLevelFilter knowledgeLevel, Context context) {
        textViewTitle.setText(String.format("%s (%s)", knowledgeLevel.getKnowledgeLevelEnum().getKnowledgeLevel(context), knowledgeLevel.getWordsCountInKnowledgeLevel()));
        int backgroundResource;
        switch(knowledgeLevel.getKnowledgeLevelEnum()) {
            case NEW:
                backgroundResource = R.color.red;
                break;
            case SHORT_TERM_MEMORY:
                backgroundResource = R.color.orange;
                break;
            case MID_TERM_MEMORY:
                backgroundResource = R.color.yellow;
                break;
            case LONG_TERM_MEMORY:
                backgroundResource = R.color.green;
                break;
            default:
                backgroundResource = R.color.white;
                break;
        }
        if(knowledgeLevel.getSelected()) {
            itemTitleView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                    backgroundResource));
            textViewTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
       } else {
            itemTitleView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                    R.color.white));
            textViewTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), backgroundResource));
        }
    }
}
