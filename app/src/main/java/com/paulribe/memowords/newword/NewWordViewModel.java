package com.paulribe.memowords.newword;

import com.paulribe.memowords.BaseViewModel;
import com.paulribe.memowords.common.firebase.FirebaseDataHelper;
import com.paulribe.memowords.common.model.Word;

import java.util.List;

public class NewWordViewModel extends BaseViewModel {

    private Word newWord;
    private List<Word> words;

    @Override
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
        newWord.setWordTranslated(wordDE);
        newWord.setWordNative(wordFR);
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
