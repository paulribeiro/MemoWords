package com.paulribe.memowords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

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

        mContext = new mContext();

        cpt = 0;
        textViewWord = view.findViewById(R.id.textview_word);
        textViewTranslation = view.findViewById(R.id.textview_translation);
        showAnswerButton = view.findViewById(R.id.button);
        easyButton = view.findViewById(R.id.button2);
        difficultButton = view.findViewById(R.id.button3);
        easyButton.setVisibility(View.GONE);
        difficultButton.setVisibility(View.GONE);
        textViewTranslation.setVisibility(View.GONE);

        textViewWord.setText(mContext.getWords().get(cpt).getWordFR());
        textViewTranslation.setText(mContext.getWords().get(cpt).getWordDE());

        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerButton.setVisibility(View.GONE);
                easyButton.setVisibility(View.VISIBLE);
                difficultButton.setVisibility(View.VISIBLE);
                textViewTranslation.setVisibility(View.VISIBLE);

            }
        });

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getFirebaseDataHelper().updateWord(mContext.getWords().get(cpt));
                cpt++;
                showAnswerButton.setVisibility(View.VISIBLE);
                easyButton.setVisibility(View.GONE);
                difficultButton.setVisibility(View.GONE);
                textViewWord.setText(mContext.getWords().get(cpt).getWordDE());
                textViewTranslation.setText(mContext.getWords().get(cpt).getWordFR());
                textViewTranslation.setVisibility(View.GONE);
            }
        });

        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpt++;
                showAnswerButton.setVisibility(View.VISIBLE);
                easyButton.setVisibility(View.GONE);
                difficultButton.setVisibility(View.GONE);
                textViewWord.setText(mContext.getWords().get(cpt).getWordDE());
                textViewTranslation.setText(mContext.getWords().get(cpt).getWordFR());
                textViewTranslation.setVisibility(View.GONE);
            }
        });

    }
}
