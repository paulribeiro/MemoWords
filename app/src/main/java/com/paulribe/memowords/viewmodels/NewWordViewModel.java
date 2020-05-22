package com.paulribe.memowords.viewmodels;

import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.util.List;

public class NewWordViewModel extends BaseViewModel {

    public final long NO_LAST_SUCCESS = 946684800;
    public final long NO_LAST_TRY = 915148800;
    private Word newWord;
    private List<Word> words;

    public void init() {
        readWords();
    }

    public void readWords() {
        firebaseDataHelper.setReferenceWords(getCurrentLanguage().getValue(), getCurrentUser());
        firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

            @Override
            public void dataIsLoaded(List<Word> w, List<String> keys) {
                words = w;
            }

            @Override
            public void dataIsInserted() {}

            @Override
            public void dataIsUpdated(List<Word> w) {
                words = w;
            }

            @Override
            public void dataIsDeleted() {

            }
        });
    }

    public Word getNewWord() {
        return newWord;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setNewWord(Word newWord) {
        this.newWord = newWord;
    }

    public void updateWord(String wordDE, String wordFR, String context) {
        newWord.setWordDE(wordDE);
        newWord.setWordFR(wordFR);
        newWord.setContext(context);
        firebaseDataHelper.updateWord(newWord);
    }

    public void addWord(Word word) {
        firebaseDataHelper.addWord(word);
    }

    public void deleteWord() {
        firebaseDataHelper.deleteWord(newWord);
    }
}
