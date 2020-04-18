package com.paulribe.memowords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.util.ArrayList;

import java.util.List;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view for activity
        //setContentView(R.layout.activity_splash);

        final mContext mContext = new mContext();
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