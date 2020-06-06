package com.paulribe.memowords.recyclerViews.knowledgeLevelFilter;

import android.view.View;
import android.widget.TextView;
import com.paulribe.memowords.R;
import com.paulribe.memowords.model.KnowledgeLevelFilter;

import androidx.recyclerview.widget.RecyclerView;

public class KnowledgeLevelViewHolder extends RecyclerView.ViewHolder {

    private View itemTitleView;
    private TextView textViewTitle;


    public KnowledgeLevelViewHolder(View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textView_title);
        itemTitleView = itemView.findViewById(R.id.item_filter_view);
    }

    public void update(KnowledgeLevelFilter knowledgeLevel) {
        textViewTitle.setText(knowledgeLevel.getKnowledgeLevelEnum().getKnowledgeLevel());
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
                backgroundResource = R.color.red;
                break;
        }
        if(knowledgeLevel.getSelected()) {
            itemTitleView.setBackgroundTintList(itemView.getContext().getResources().getColorStateList(backgroundResource));
            textViewTitle.setTextColor(itemView.getContext().getResources().getColorStateList(R.color.white));
        } else {
            itemTitleView.setBackgroundTintList(itemView.getContext().getResources().getColorStateList(R.color.white));
            textViewTitle.setTextColor(itemView.getContext().getResources().getColorStateList(backgroundResource));
        }
    }
}
