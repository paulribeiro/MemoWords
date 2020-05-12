package com.paulribe.memowords.model;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.enumeration.LanguageEnum;
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
    private static List<Word> wordsToDisplay;
    private static boolean isRevisionFinished;
    private static FirebaseDataHelper firebaseDataHelper;
    public static final long NO_LAST_SUCCESS = 946684800;
    public static final long NO_LAST_TRY = 915148800;
    private static LanguageEnum currentLanguage = LanguageEnum.GERMAN;
    private static FirebaseUser currentUser;

    public mContext() {

    }

    public static List<Word> getWords() {
        return words;
    }

    public static void setWords(List<Word> words) {
        mContext.words = words;
        calculateWordsToRevise();
        calculateWordsToLearn();
    }

    public static FirebaseDataHelper getFirebaseDataHelper() {
        return firebaseDataHelper;
    }

    public static void setFirebaseDataHelper(FirebaseDataHelper firebaseDataHelper) {
        mContext.firebaseDataHelper = firebaseDataHelper;
    }

    public static List<Word> getWordsToDisplay() {
        return wordsToDisplay;
    }

    public static void setWordsToDisplay(List<Word> wordsToDisplay) {
        mContext.wordsToDisplay = wordsToDisplay;
    }

    public static boolean getIsRevisionFinished() {
        return isRevisionFinished;
    }

    public static void setIsRevisionFinished(boolean isRevisionFinished) {
        mContext.isRevisionFinished = isRevisionFinished;
    }

    public static void calculateWordsToLearn() {
        if(isRevisionFinished) {
            List<Word> wordsToOrder = new ArrayList<>(words);
            wordsToDisplay = wordsToOrder.stream().filter(w -> w.getNumberTry().equals(0)).collect(Collectors.toList());
        }
    }

    private static void calculateWordsToRevise() {
        List<Word> wordsToOrder = new ArrayList<>(words);
        Collections.sort(wordsToOrder, Comparator.comparing(Word::getKnowledgeLevel)
                .thenComparing(Word::getDateAdded));
        wordsToDisplay = wordsToOrder.stream().filter(w -> !w.getNumberTry().equals(0) && hasToBeRevise(w)).collect(Collectors.toList());
        isRevisionFinished = CollectionUtils.isEmpty(wordsToDisplay);
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

    public static void addWord(Word word) {
        firebaseDataHelper.addWord(word);
    }

    public static void updateWord(Word word) {
        firebaseDataHelper.updateWord(word);
    }

    public static LanguageEnum getCurrentLanguage() {
        return currentLanguage;
    }

    public static void setCurrentLanguage(LanguageEnum language) {
        currentLanguage = language;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(FirebaseUser currentUser) {
        mContext.currentUser = currentUser;
    }
}
