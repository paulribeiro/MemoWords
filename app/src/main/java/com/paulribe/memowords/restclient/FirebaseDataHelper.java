package com.paulribe.memowords.restclient;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.Query;
import com.paulribe.memowords.model.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class FirebaseDataHelper implements Serializable {
    private FirebaseDatabase dataBase;
    private DatabaseReference referenceWords;
    private List<Word> words = new ArrayList<>();
    private ValueEventListener listener;
    private Integer cpt = 0;

    public FirebaseDataHelper() {
        dataBase = FirebaseDatabase.getInstance();
        dataBase.setPersistenceEnabled(true);
        referenceWords = dataBase.getReference("user1/words/german");
        referenceWords.keepSynced(true);
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

    public void updateWord(final Word word) {
        word.setNumberSuccess(word.getNumberSuccess() + 1);
        referenceWords.child(word.getId().toString()).setValue(word);
    }

}
