package com.paulribe.memowords.restclient;

import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.model.UserConfig;
import com.paulribe.memowords.model.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paulribe.memowords.viewmodels.BaseViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class FirebaseDataHelper implements Serializable {
    private FirebaseDatabase dataBase;
    private DatabaseReference referenceWords;
    private DatabaseReference referenceUserConfig;
    private List<Word> words = new ArrayList<>();
    private Integer cpt = 0;
    private ValueEventListener configListener;
    private ValueEventListener wordsListener;

    public FirebaseDataHelper() {
        if(dataBase == null) {
            dataBase = FirebaseDatabase.getInstance();
            dataBase.setPersistenceEnabled(true);
        }
    }

    public interface DataStatus {
        void dataIsLoaded(List<Word> words, List<String> keys);
        void dataIsInserted();
        void dataIsUpdated(List<Word> words);
        void dataIsDeleted();
    }

    public interface UserConfigLoadedStatus {
        void UserConfigIsLoaded(UserConfig userConfig);
    }

    public void readWords(final DataStatus dataStatus) {
        //TODO : check if doesn't happen many times ...
        referenceWords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                words.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Word word = keyNode.getValue(Word.class);
                    if (word != null) {
                        word.setId(Integer.parseInt(Objects.requireNonNull(keyNode.getKey())));
                    }
                    words.add(word);
                }
                if(cpt == 0 ) {
                    dataStatus.dataIsLoaded(words, keys);
                    cpt ++;
                } else {
                    dataStatus.dataIsUpdated(words);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadUserConfig(final UserConfigLoadedStatus userConfigLoadedStatus) {
        if(configListener != null) {
            referenceUserConfig.removeEventListener(configListener);
        }
        configListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserConfig userConfig = dataSnapshot.getValue(UserConfig.class);
                userConfigLoadedStatus.UserConfigIsLoaded(userConfig);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        referenceUserConfig.addValueEventListener(configListener);
    }

    public void setWordEasy(final Word word) {
        Date now = new Date();
        word.setLastSuccess(now.getTime());
        word.setLastTry(now.getTime());
        word.setNumberSuccess(word.getNumberSuccess() + 1);
        word.setNumberTry(word.getNumberTry() + 1);
        word.setKnowledgeLevel(word.getKnowledgeLevel() + 1);
        referenceWords.child(word.getId().toString()).setValue(word);
    }

    public void setWordDifficult(final Word word) {
        Date now = new Date();
        word.setLastTry(now.getTime());
        word.setNumberTry(word.getNumberTry() + 1);
        word.setKnowledgeLevel(0);
        referenceWords.child(word.getId().toString()).setValue(word);
    }

    public void addWord(final Word word) {
        int id = words.size() + 1;
        referenceWords.child(Integer.toString(id)).setValue(word);
    }

    public void updateWord(final Word word) {
        int id = word.getId();
        referenceWords.child(Integer.toString(id)).setValue(word);
    }

    public void deleteWord(final Word word) {
        int id = word.getId();
        referenceWords.child(Integer.toString(id)).removeValue();
    }

    public DatabaseReference getReferenceWords() {
        return referenceWords;
    }

    public void setReferenceWords(LanguageEnum languageEnum) {
        referenceWords = dataBase.getReference(BaseViewModel.getCurrentUser().getUid() + "/words/" + languageEnum.getLanguage());
        referenceWords.keepSynced(true);
    }

    public void setReferenceUserConfig() {
        referenceUserConfig = dataBase.getReference(BaseViewModel.getCurrentUser().getUid() + "/config");
        referenceUserConfig.keepSynced(true);
    }

    public void updateCurrentLanguage(LanguageEnum language) {
        referenceUserConfig.child("currentLanguage").setValue(language.name());
    }

    public void addUser() {
        referenceUserConfig.child("/currentLanguage/").setValue("GERMAN");
    }
}
