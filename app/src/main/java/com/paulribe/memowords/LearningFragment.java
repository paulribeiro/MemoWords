package com.paulribe.memowords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.paulribe.memowords.model.mContext;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LearningFragment extends Fragment {

    private int cpt;
    private TextView textViewWord;
    private TextView textViewTranslation;
    private boolean answerShown = false;
    private Button showAnswerButton;
    private Button easyButton;
    private Button difficultButton;
    private mContext mContext;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        defineViews(view);

        mContext = new mContext();
        cpt = 0;
        easyButton.setVisibility(View.GONE);
        difficultButton.setVisibility(View.GONE);
        textViewTranslation.setVisibility(View.GONE);
        textViewWord.setText(mContext.getWordsToLearn().get(cpt).getWordFR());
        textViewTranslation.setText(mContext.getWordsToLearn().get(cpt).getWordDE());

        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerAction();

            }
        });

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getFirebaseDataHelper().setWordEasy(mContext.getWordsToLearn().get(cpt));
                cpt++;
                afterAnswerAction();
            }
        });

        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getFirebaseDataHelper().setWordDifficult(mContext.getWordsToLearn().get(cpt));
                cpt++;
                afterAnswerAction();
            }
        });

    }

    private void defineViews(@NonNull View view) {
        textViewWord = view.findViewById(R.id.textview_word);
        textViewTranslation = view.findViewById(R.id.textview_translation);
        showAnswerButton = view.findViewById(R.id.button);
        easyButton = view.findViewById(R.id.button2);
        difficultButton = view.findViewById(R.id.button3);
    }

    private void showAnswerAction() {
        showAnswerButton.setVisibility(View.GONE);
        easyButton.setVisibility(View.VISIBLE);
        difficultButton.setVisibility(View.VISIBLE);
        textViewTranslation.setVisibility(View.VISIBLE);
    }

    private void afterAnswerAction() {
        showAnswerButton.setVisibility(View.VISIBLE);
        easyButton.setVisibility(View.GONE);
        difficultButton.setVisibility(View.GONE);
        textViewWord.setText(mContext.getWordsToLearn().get(cpt).getWordFR());
        textViewTranslation.setText(mContext.getWordsToLearn().get(cpt).getWordDE());
        textViewTranslation.setVisibility(View.GONE);
    }
}
