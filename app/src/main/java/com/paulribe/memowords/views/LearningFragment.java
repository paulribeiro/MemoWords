package com.paulribe.memowords.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.paulribe.memowords.R;
import com.paulribe.memowords.enumeration.LearningFragmentStateEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.viewmodels.LearningViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class LearningFragment extends Fragment {

    private TextView textViewWord;
    private TextView textViewTranslation;
    private TextView textViewContext;
    private Button showAnswerButton;
    private Button easyButton;
    private Button difficultButton;
    private View layoutNew;
    private ImageButton editWordButton;

    private LearningViewModel learningViewModel;

    public LearningViewModel getLearningViewModel() {
        return learningViewModel;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learning, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataBinding();

        defineViews(view);
        setElementVisibility(true);
        setButtonsOnClickListener();
        layoutNew.setVisibility(View.VISIBLE);
        if (!learningViewModel.getIsRevisionFinished().getValue()) {
            layoutNew.setVisibility(View.GONE);
        }
    }

    private void setButtonsOnClickListener() {
        showAnswerButton.setOnClickListener(view -> showAnswerButtonClicked());

        easyButton.setOnClickListener(view -> learningViewModel.setWordEasy());

        difficultButton.setOnClickListener(view -> learningViewModel.setWordDifficult());

        editWordButton.setOnClickListener(view -> {
            ((MainActivity)getActivity()).switchToNewWordFragment(learningViewModel.getCurrentWord().getValue());
        });
    }

    private void showAnswerButtonClicked() {
        setElementVisibility(false);
    }

    private void setTextViewWithNextWord() {
        textViewWord.setText(learningViewModel.getCurrentWord().getValue().getWordFR());
        textViewTranslation.setText(learningViewModel.getCurrentWord().getValue().getWordDE());
        textViewContext.setText(learningViewModel.getCurrentWord().getValue().getContext());
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

    private void setElementVisibility(boolean isBeforeShowAnswerClicked) {
        showAnswerButton.setVisibility(isBeforeShowAnswerClicked ? View.VISIBLE : View.GONE);
        easyButton.setVisibility(isBeforeShowAnswerClicked ? View.GONE : View.VISIBLE);
        difficultButton.setVisibility(isBeforeShowAnswerClicked ? View.GONE : View.VISIBLE);
        textViewTranslation.setVisibility(isBeforeShowAnswerClicked ? View.GONE : View.VISIBLE);
    }

    private void initDataBinding() {
        //FragmentLearningBinding fragmentLearningBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_learning);
        learningViewModel = new ViewModelProvider(getActivity()).get(LearningViewModel.class);
        //fragmentLearningBinding.setLearningViewModel(learningViewModel);
        learningViewModel.init();
        setUpChangeValueListener();
        learningViewModel.readWords();
    }

    private void setUpChangeValueListener() {
        learningViewModel.getCurrentWord().observe(getViewLifecycleOwner(), this::onCurrentWordChanged);
        learningViewModel.getLearningFragmentStateEnum().observe(getViewLifecycleOwner(), this::onLearningFragmentStateChanged);
    }

    private void onLearningFragmentStateChanged(LearningFragmentStateEnum learningFragmentStateEnum) {
        switch(learningFragmentStateEnum) {
            case LEARNING_FRAGMENT:
                ((MainActivity)getActivity()).displayLearningFragment(false);
                break;
            case NO_MORE_WORDS:
                ((MainActivity)getActivity()).displayNoMoreWords(false);
                break;
            case REVISION_FINISHED:
                ((MainActivity)getActivity()).displayRevisionFinished(false);
                break;
        }
    }

    @VisibleForTesting
    public void onCurrentWordChanged(Word word) {
        setElementVisibility(true);
        setTextViewWithNextWord();
        layoutNew.setVisibility(View.VISIBLE);
        if (!learningViewModel.getIsRevisionFinished().getValue()) {
            layoutNew.setVisibility(View.GONE);
            int nbWordToRevise = learningViewModel.getWordsToDisplay().size();
            if (nbWordToRevise > 0) {
                ((MainActivity) getActivity()).setBadgeText(Integer.toString(nbWordToRevise));
            } else {
                ((MainActivity) getActivity()).deleteBadge();
            }
        }
    }
}