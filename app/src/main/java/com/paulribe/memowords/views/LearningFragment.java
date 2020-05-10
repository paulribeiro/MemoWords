package com.paulribe.memowords.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.R;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LearningFragment extends Fragment {

    private TextView textViewWord;
    private TextView textViewTranslation;
    private TextView textViewContext;
    private Button showAnswerButton;
    private Button easyButton;
    private Button difficultButton;
    private View layoutNew;
    private ImageButton editWordButton;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        defineViews(view);
        setElementVisibility(true);
        setButtonsOnClickListener();
        layoutNew.setVisibility(View.VISIBLE);
        if (!mContext.getIsRevisionFinished()) {
            layoutNew.setVisibility(View.GONE);
        }
        if(!CollectionUtils.isEmpty(mContext.getWordsToDisplay())) {
            setTextViewWithNextWord();
        }
    }

    private void setButtonsOnClickListener() {
        showAnswerButton.setOnClickListener(view -> showAnswerButtonClicked());

        easyButton.setOnClickListener(view -> {
            mContext.getFirebaseDataHelper().setWordEasy(getCurrentWord());
            mContext.getWordsToDisplay().remove(0);
            updateWordList();
        });

        difficultButton.setOnClickListener(view -> {
            mContext.getFirebaseDataHelper().setWordDifficult(getCurrentWord());
            mContext.getWordsToDisplay().remove(0);
            updateWordList();
        });

        editWordButton.setOnClickListener(view -> {
            ((MainActivity)getActivity()).switchToNewWordFragment(getCurrentWord());
        });
    }

    public void updateWordList() {
        if(mContext.getIsRevisionFinished()) {
            if(mContext.getWordsToDisplay().size() > 0) {
                ((MainActivity)getActivity()).displayLearningFragment();
                displayNextWord();
            } else {
                ((MainActivity)getActivity()).displayNoMoreWords();
            }
        } else {
            if(mContext.getWordsToDisplay().size() > 0) {
                ((MainActivity)getActivity()).displayLearningFragment();
                displayNextWord();
            } else {
                ((MainActivity)getActivity()).displayRevisionFinished();
            }
        }
    }

    private void showAnswerButtonClicked() {
        setElementVisibility(false);
    }

    public void displayNextWord() {
        setElementVisibility(true);
        setTextViewWithNextWord();
        layoutNew.setVisibility(View.VISIBLE);
        if (!mContext.getIsRevisionFinished()) {
            layoutNew.setVisibility(View.GONE);
            int nbWordToRevise = mContext.getWordsToDisplay().size();
            if (nbWordToRevise > 0) {
                ((MainActivity) getActivity()).setBadgeText(Integer.toString(nbWordToRevise));
            }
        }
    }

    private void setTextViewWithNextWord() {
        textViewWord.setText(getCurrentWord().getWordFR());
        textViewTranslation.setText(getCurrentWord().getWordDE());
        textViewContext.setText(getCurrentWord().getContext());
    }

    private void defineViews(@NonNull View view) {
        textViewWord = view.findViewById(R.id.textview_word);
        textViewTranslation = view.findViewById(R.id.textview_translation);
        textViewContext = view.findViewById(R.id.textViewContext);
        showAnswerButton = view.findViewById(R.id.button);
        easyButton = view.findViewById(R.id.button2);
        difficultButton = view.findViewById(R.id.button3);
        layoutNew = view.findViewById(R.id.newLayout);
        editWordButton = view.findViewById(R.id.editWordButton);
    }

    private Word getCurrentWord() {
        return mContext.getWordsToDisplay().get(0);
    }

    private void setElementVisibility(boolean isBeforeShowAnswerClicked) {
        showAnswerButton.setVisibility(isBeforeShowAnswerClicked ? View.VISIBLE : View.GONE);
        easyButton.setVisibility(isBeforeShowAnswerClicked ? View.GONE : View.VISIBLE);
        difficultButton.setVisibility(isBeforeShowAnswerClicked ? View.GONE : View.VISIBLE);
        textViewTranslation.setVisibility(isBeforeShowAnswerClicked ? View.GONE : View.VISIBLE);
    }
}
