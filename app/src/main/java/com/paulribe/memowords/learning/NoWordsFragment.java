package com.paulribe.memowords.learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.paulribe.memowords.R;

public class NoWordsFragment extends Fragment {

    public NoWordsFragment() {
        // No action to perform.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_words, container, false);
    }
}
