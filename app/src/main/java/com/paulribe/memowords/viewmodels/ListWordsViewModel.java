package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.MutableLiveData;

public class ListWordsViewModel extends BaseViewModel {

    private MutableLiveData<List<Word>> words;
    private MutableLiveData<OrderByEnum> orderByEnum;
    private MutableLiveData<Boolean> isFavoriteSelected;
    private MutableLiveData<String> searchedString;


    public MutableLiveData<List<Word>> getWords() {
        return words;
    }

    public MutableLiveData<OrderByEnum> getOrderByEnum() {
        return orderByEnum;
    }

    public MutableLiveData<Boolean> getIsFavoriteSelected() {
        return isFavoriteSelected;
    }

    public MutableLiveData<String> getSearchedString() {
        return searchedString;
    }

    public void init() {
        words = new MutableLiveData<>(new ArrayList<>());
        orderByEnum = new MutableLiveData<>(OrderByEnum.LAST_TRY);
        isFavoriteSelected = new MutableLiveData<>(Boolean.FALSE);
        searchedString = new MutableLiveData<>("");
    }

    public void readWords() {

        firebaseDataHelper.setReferenceWords(getCurrentLanguage().getValue(), getCurrentUser());
        firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

            @Override
            public void dataIsLoaded(List<Word> w, List<String> keys) {
                words.setValue(w);
            }

            @Override
            public void dataIsInserted() {}

            @Override
            public void dataIsUpdated(List<Word> w) {
                words.setValue(w);
            }

            @Override
            public void dataIsDeleted() {}
        });
    }
}
