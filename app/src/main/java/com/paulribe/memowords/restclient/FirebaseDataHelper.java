package com.paulribe.memowords.restclient;

import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.model.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paulribe.memowords.model.mContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class FirebaseDataHelper implements Serializable {
    private FirebaseDatabase dataBase;
    private DatabaseReference referenceWords;
    private static DatabaseReference referenceBase;
    private List<Word> words = new ArrayList<>();
    private ValueEventListener listener;
    private Integer cpt = 0;

    public FirebaseDataHelper() {
        if(dataBase == null) {
            dataBase = FirebaseDatabase.getInstance();
            dataBase.setPersistenceEnabled(true);
            referenceBase = dataBase.getReference();
        }
    }

    public interface DataStatus {
        void dataIsLoaded(List<Word> words, List<String> keys);
        void dataIsInserted();
        void dataIsUpdated(List<Word> words);
        void dataIsDeleted();

    }

    public void readWords(final DataStatus dataStatus) {
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

    public DatabaseReference getReferenceWords() {
        return referenceWords;
    }

    public void setReferenceWords(LanguageEnum languageEnum) {
        referenceWords = dataBase.getReference(mContext.getCurrentUser().getUid() + "/words/" + languageEnum.getLanguage());
        referenceWords.keepSynced(true);
    }

    public static void addUser() {
        referenceBase.child(mContext.getCurrentUser().getUid() + "/words/german");
    }
}
