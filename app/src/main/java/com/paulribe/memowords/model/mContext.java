package com.paulribe.memowords.model;

import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.io.Serializable;
import java.util.List;

public class mContext implements Serializable {
    private static  List<Word> words;
    private static FirebaseDataHelper firebaseDataHelper;

    public mContext() {

    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        mContext.words = words;
    }

    public FirebaseDataHelper getFirebaseDataHelper() {
        return firebaseDataHelper;
    }

    public void setFirebaseDataHelper(FirebaseDataHelper firebaseDataHelper) {
        mContext.firebaseDataHelper = firebaseDataHelper;
    }
}
