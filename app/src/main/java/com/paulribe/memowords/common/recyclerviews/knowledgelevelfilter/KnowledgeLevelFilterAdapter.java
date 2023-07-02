package com.paulribe.memowords.common.recyclerviews.knowledgelevelfilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;
import com.paulribe.memowords.common.model.KnowledgeLevelFilter;

import java.util.List;

public class KnowledgeLevelFilterAdapter extends RecyclerView.Adapter<KnowledgeLevelFilterViewHolder> {

    private List<KnowledgeLevelFilter> filters;
    private final OnViewClickListener listener;

    public KnowledgeLevelFilterAdapter(List<KnowledgeLevelFilter> filters, OnViewClickListener listener) {
        this.filters = filters;
        this.listener = listener;
    }

    public interface OnViewClickListener {
        void onViewClick(KnowledgeLevelFilter knowledgeLevelFilter);
    }

    @NonNull
    @Override
    public KnowledgeLevelFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.knowledge_level_item, parent, false);
        return new KnowledgeLevelFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeLevelFilterViewHolder holder, int position) {
        holder.update(this.filters.get(position), holder.itemView.getContext());

        holder.itemView.setOnClickListener(v -> listener.onViewClick(filters.get(position)));
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public void setFilters(List<KnowledgeLevelFilter> filters) {
        this.filters = filters;
    }
}