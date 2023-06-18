package com.paulribe.memowords.common.recyclerViews.knowledgeLevelFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.KnowledgeLevelFilter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KnowledgeLevelFilterAdapter extends RecyclerView.Adapter<KnowledgeLevelViewHolder> {

    private List<KnowledgeLevelFilter> filters;
    private OnViewClickListener listener;

    public KnowledgeLevelFilterAdapter(List<KnowledgeLevelFilter> filters, OnViewClickListener listener) {
        this.filters = filters;
        this.listener = listener;
    }

    public interface OnViewClickListener {
        void onViewClick(KnowledgeLevelFilter knowledgeLevelFilter);
    }

    @NonNull
    @Override
    public KnowledgeLevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.knowledge_level_item, parent, false);
        return new KnowledgeLevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeLevelViewHolder holder, int position) {
        holder.update(this.filters.get(position), holder.itemView.getContext());

        holder.itemView.setOnClickListener(v -> listener.onViewClick(filters.get(position)));
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }
}