package com.paulribe.memowords.model;

import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
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
                return word.getLastTry().before(yesterday);
            case 2:
                Date lastWeek = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 7));
                return word.getLastTry().before(lastWeek);
            case 3:
                Date lastMonth = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 21));
                return word.getLastTry().before(lastMonth);
            default:
                return false;
        }
    }
}
