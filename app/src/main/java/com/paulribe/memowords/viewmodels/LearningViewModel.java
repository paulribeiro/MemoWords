package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.enumeration.LearningFragmentStateEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.restclient.FirebaseDataHelper;
import java.util.ArrayList;
import java.util.Calendar;
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
    private MutableLiveData<Boolean> isDataLoaded;

    public void init() {
        currentWord = new MutableLiveData<>();
        wordsToDisplay = new ObservableArrayList<>();
        isRevisionFinished = new MutableLiveData<>(Boolean.FALSE);
        learningFragmentStateEnum = new MutableLiveData<>(LearningFragmentStateEnum.LEARNING_FRAGMENT);
        isDataLoaded = new MutableLiveData<>(Boolean.FALSE);
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

    public MutableLiveData<Boolean> getIsDataLoaded() {
        return isDataLoaded;
    }

    public MutableLiveData<Boolean> getIsRevisionFinished() {
        return isRevisionFinished;
    }

    public void readWords() {
        firebaseDataHelper.setReferenceWords(getCurrentLanguage().getValue(), getCurrentUser());
        firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

            @Override
            public void dataIsLoaded(List<Word> w, List<String> keys) {
                words = w;
                calculateWordsToRevise();
                calculateWordsToLearn();
                updateLearningState();
                if(!isDataLoaded.getValue()) {
                    isDataLoaded.setValue(Boolean.TRUE);
                }
            }

            @Override
            public void dataIsInserted() {}

            @Override
            public void dataIsUpdated(List<Word> w) {
                words = w;
                calculateWordsToRevise();
                calculateWordsToLearn();
                updateLearningState();
                if(!isDataLoaded.getValue()) {
                    isDataLoaded.setValue(Boolean.TRUE);
                }
            }

            @Override
            public void dataIsDeleted() {

            }
        });
    }

    public void calculateWordsToLearn() {
        if(isRevisionFinished.getValue()) {
            List<Word> wordsToOrder = new ArrayList<>(words);
            Collections.sort(wordsToOrder, Comparator.comparing(Word::getDateAdded).reversed());
            wordsToDisplay = wordsToOrder.stream().filter(w -> w.getNumberTry().equals(0)).collect(Collectors.toList());
        }
    }

    private void calculateWordsToRevise() {
        if(!isRevisionFinished.getValue()) {
            List<Word> wordsToOrder = new ArrayList<>(words);
            Collections.sort(wordsToOrder, Comparator.comparing(Word::getKnowledgeLevel)
                    .thenComparing(Word::getDateAdded).reversed());
            wordsToDisplay = wordsToOrder.stream().filter(w -> !w.getNumberTry().equals(0) && hasToBeRevise(w)).collect(Collectors.toList());
            //isRevisionFinished.setValue(CollectionUtils.isEmpty(wordsToDisplay));
        }
    }

    private static boolean hasToBeRevise(Word word) {
        Date today = getTodayDateRevision();
        // TODO : use LocalDateTime ?
        switch(word.getKnowledgeLevel()) {
            case 0:
            case 1:
                Date yesterday = new Date(today.getTime() - (1000 * 60 * 60 * 24));
                return word.getLastTry() < yesterday.getTime();
            case 2:
            case 3:
                Date lastWeek = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 7));
                return word.getLastTry() < lastWeek.getTime();
            case 4:
                Date lastMonth = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 21));
                return word.getLastTry() < lastMonth.getTime();
            default:
                return false;
        }
    }

    private static Date getTodayDateRevision() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        return now.getTime();
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

    public void updateLearningState() {
        if(wordsToDisplay.size() > 0) {
            learningFragmentStateEnum.setValue(LearningFragmentStateEnum.LEARNING_FRAGMENT);
            currentWord.setValue(wordsToDisplay.get(0));
        } else {
            if(isRevisionFinished.getValue()) {
                learningFragmentStateEnum.setValue(LearningFragmentStateEnum.NO_MORE_WORDS);
            } else {
                learningFragmentStateEnum.setValue(LearningFragmentStateEnum.REVISION_FINISHED);
            }
        }
    }
}
