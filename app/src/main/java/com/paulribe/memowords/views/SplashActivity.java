package com.paulribe.memowords.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;
import com.paulribe.memowords.restclient.FirebaseDataHelper;
import java.util.List;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FirebaseDataHelper firebaseDataHelper = new FirebaseDataHelper();
        firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

            @Override
            public void dataIsLoaded(List<Word> w, List<String> keys) {
                mContext.setWords(w);
                mContext.setFirebaseDataHelper(firebaseDataHelper);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // close this activity
                finish();
            }

            @Override
            public void dataIsInserted() {

            }

            @Override
            public void dataIsUpdated(List<Word> w) {
                mContext.setWords(w);
            }

            @Override
            public void dataIsDeleted() {

            }
        });
    }
}