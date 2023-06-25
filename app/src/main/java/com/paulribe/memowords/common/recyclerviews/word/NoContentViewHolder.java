package com.paulribe.memowords.common.recyclerviews.word;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.paulribe.memowords.R;

public class NoContentViewHolder extends RecyclerView.ViewHolder {

    private final ImageView noResultSearchWord;
    private final TextView trySearchingAgainTextView;
    private final TextView noResultTextView;

    public NoContentViewHolder(View itemView) {
        super(itemView);
        this.trySearchingAgainTextView = itemView.findViewById(R.id.trySearchingAgainTextView);
        this.noResultTextView = itemView.findViewById(R.id.noMatchesTextView);
        this.noResultSearchWord = itemView.findViewById(R.id.noResultSearchWord);
    }

    public void updateNoContentItemWithoutImage() {
        noResultSearchWord.setVisibility(View.GONE);
        trySearchingAgainTextView.setVisibility(View.GONE);
        noResultTextView.setVisibility(View.VISIBLE);
        noResultTextView.setText(R.string.no_results);
    }

    public void updateNoContentItemWithImage(String searchedWord, Context context) {
        noResultSearchWord.setVisibility(View.VISIBLE);
        trySearchingAgainTextView.setVisibility(View.VISIBLE);
        noResultTextView.setVisibility(View.VISIBLE);
        noResultSearchWord.setImageResource(R.drawable.no_matches_transparent);
        trySearchingAgainTextView.setText(context.getResources().getString(R.string.please_try_searching_another_term));
        String text = String.format(context.getResources().getString(R.string.sorry_we_couldn_t_find_any_matches), searchedWord);
        SpannableStringBuilder str = new SpannableStringBuilder(text);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), text.indexOf(searchedWord), text.indexOf(searchedWord) + searchedWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        noResultTextView.setText(str);

    }
}
