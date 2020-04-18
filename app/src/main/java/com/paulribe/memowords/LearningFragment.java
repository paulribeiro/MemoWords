package com.paulribe.memowords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LearningFragment extends Fragment {

    private TextView textViewWord;
    private TextView textViewTranslation;
    private Button showAnswerButton;
    private Button easyButton;
    private Button difficultButton;

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

        easyButton.setVisibility(View.GONE);
        difficultButton.setVisibility(View.GONE);
        textViewTranslation.setVisibility(View.GONE);

        if(CollectionUtils.isEmpty(mContext.getWordsToDisplay())) {
            // TODO : show image no content.
        } else {
            textViewWord.setText(getCurrentWord().getWordFR());
            textViewTranslation.setText(getCurrentWord().getWordDE());
        }


        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerAction();

            }
        });

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getFirebaseDataHelper().setWordEasy(getCurrentWord());
                updateWordList();
                afterAnswerAction();
            }
        });

        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getFirebaseDataHelper().setWordDifficult(getCurrentWord());
                updateWordList();
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
        textViewWord.setText(getCurrentWord().getWordFR());
        textViewTranslation.setText(getCurrentWord().getWordDE());
        textViewTranslation.setVisibility(View.GONE);
        if(!mContext.getIsRevisionFinished()) {
            int nbWordToRevise = mContext.getWordsToDisplay().size();
            if(nbWordToRevise > 0) {
                ((MainActivity)getActivity()).setBadgeText(Integer.toString(nbWordToRevise));
            }
        }
    }

    private Word getCurrentWord() {
        return mContext.getWordsToDisplay().get(0);
    }

    private void updateWordList() {
        if(mContext.getIsRevisionFinished()) {
            if(mContext.getWordsToDisplay().size() > 1) {
                mContext.getWordsToDisplay().remove(0);
            } else {
                //TODO : show no more words ?
            }
        } else {
            if(mContext.getWordsToDisplay().size() > 1) {
                mContext.getWordsToDisplay().remove(0);
            } else {
                //TODO : do something : ask if want to learn new words ?
                mContext.setIsRevisionFinished(true);
                ((MainActivity)getActivity()).deleteBadge();
                mContext.calculateWordsToLearn();
            }

        }
    }
}
