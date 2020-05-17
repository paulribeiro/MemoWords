package com.paulribe.memowords.viewmodels;

import com.google.android.gms.common.util.CollectionUtils;
import com.paulribe.memowords.enumeration.LearningFragmentStateEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.restclient.FirebaseDataHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LearningViewModel extends BaseViewModel {

    private List<Word> words;
    private List<Word> wordsToDisplay;
    private MutableLiveData<Word> currentWord;
    private MutableLiveData<Boolean> isRevisionFinished;
    private MutableLiveData<LearningFragmentStateEnum> learningFragmentStateEnum;

    public void init() {
        currentWord = new MutableLiveData<>();
        wordsToDisplay = new ObservableArrayList<>();
        isRevisionFinished = new MutableLiveData<>(Boolean.FALSE);
        learningFragmentStateEnum = new MutableLiveData<>(LearningFragmentStateEnum.LEARNING_FRAGMENT);
    }

    public LiveData<Word> getCurrentWord() {
        return currentWord;
    }

    public List<Word> getWordsToDisplay() {
        return wordsToDisplay;
    }

    public MutableLiveData<LearningFragmentStateEnum> getLearningFragmentStateEnum() {
        return learningFragmentStateEnum;
    }

    public MutableLiveData<Boolean> getIsRevisionFinished() {
        return isRevisionFinished;
    }

    public void readWords() {
        firebaseDataHelper.setReferenceWords(getCurrentLanguage().getValue());
        firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

            @Override
            public void dataIsLoaded(List<Word> w, List<String> keys) {
                words = w;
                calculateWordsToRevise();
                calculateWordsToLearn();
                updateLearningState();
            }

            @Override
            public void dataIsInserted() {}

            @Override
            public void dataIsUpdated(List<Word> w) {
                words = w;
                calculateWordsToRevise();
                calculateWordsToLearn();
                updateLearningState();
            }

            @Override
            public void dataIsDeleted() {

            }
        });
    }

    private void calculateWordsToLearn() {
        if(isRevisionFinished.getValue()) {
            List<Word> wordsToOrder = new ArrayList<>(words);
            wordsToDisplay = wordsToOrder.stream().filter(w -> w.getNumberTry().equals(0)).collect(Collectors.toList());
        }
    }

    private void calculateWordsToRevise() {
        if(!isRevisionFinished.getValue()) {
            List<Word> wordsToOrder = new ArrayList<>(words);
            Collections.sort(wordsToOrder, Comparator.comparing(Word::getKnowledgeLevel)
                    .thenComparing(Word::getDateAdded));
            wordsToDisplay = wordsToOrder.stream().filter(w -> !w.getNumberTry().equals(0) && hasToBeRevise(w)).collect(Collectors.toList());
            isRevisionFinished.setValue(CollectionUtils.isEmpty(wordsToDisplay));
        }
    }

    private static boolean hasToBeRevise(Word word) {
        Date today = new Date();
        // TODO : use LocalDateTime ?
        switch(word.getKnowledgeLevel()) {
            case 0:
            case 1:
                Date yesterday = new Date(today.getTime() - (1000 * 60 * 60 * 24));
                return word.getLastTry() < yesterday.getTime();
            case 2:
                Date lastWeek = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 7));
                return word.getLastTry() < lastWeek.getTime();
            case 3:
                Date lastMonth = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 21));
                return word.getLastTry() < lastMonth.getTime();
            default:
                return false;
        }
    }

    public void setWordEasy() {
        firebaseDataHelper.setWordEasy(currentWord.getValue());
        wordsToDisplay.remove(0);
        updateLearningState();
    }

    public void setWordDifficult() {
        firebaseDataHelper.setWordDifficult(currentWord.getValue());
        wordsToDisplay.remove(0);
        updateLearningState();
    }

    private void updateLearningState() {
        if(wordsToDisplay.size() > 0) {
            learningFragmentStateEnum.setValue(LearningFragmentStateEnum.LEARNING_FRAGMENT);
            currentWord.setValue(wordsToDisplay.get(0));
        } else {
            if(isRevisionFinished.getValue()) {
                learningFragmentStateEnum.setValue(LearningFragmentStateEnum.NO_MORE_WORDS);
            } else {
                isRevisionFinished.setValue(true);
                learningFragmentStateEnum.setValue(LearningFragmentStateEnum.REVISION_FINISHED);
            }
        }
    }
}
