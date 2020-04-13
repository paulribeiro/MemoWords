package com.paulribe.memowords.model;

import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class mContext implements Serializable {
    private static List<Word> words;
    private static List<Word> wordsToLearn;
    private static FirebaseDataHelper firebaseDataHelper;
    public static final long NO_LAST_SUCCESS = 946684800;
    public static final long NO_LAST_TRY = 915148800;

    public mContext() {

    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        mContext.words = words;
        calculateWordsToLearn();
    }

    public FirebaseDataHelper getFirebaseDataHelper() {
        return firebaseDataHelper;
    }

    public void setFirebaseDataHelper(FirebaseDataHelper firebaseDataHelper) {
        mContext.firebaseDataHelper = firebaseDataHelper;
    }

    public List<Word> getWordsToLearn() {
        return wordsToLearn;
    }

    public void setWordsToLearn(List<Word> wordsToLearn) {
        mContext.wordsToLearn = wordsToLearn;
    }

    public void calculateWordsToLearn() {
        List<Word> wordsToOrder = new ArrayList<>(words);
        Collections.sort(wordsToOrder, Comparator.comparing(Word::getKnowledgeLevel)
                .thenComparing(Word::getDateAdded));
        wordsToLearn = wordsToOrder.stream().filter(this::hasToBeRevise).collect(Collectors.toList());
    }

    private boolean hasToBeRevise(Word word) {
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

    public void addWord(Word word) {
        firebaseDataHelper.addWord(word);
    }

    public void updateWord(Word word) {
        firebaseDataHelper.updateWord(word);
    }
}
