package com.paulribe.memowords.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;
import com.paulribe.memowords.restclient.FirebaseDataHelper;
import java.util.List;

public class SplashActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {


            final FirebaseDataHelper firebaseDataHelper = new FirebaseDataHelper();
            mContext.setCurrentUser(currentUser);
            firebaseDataHelper.setReferenceWords(LanguageEnum.GERMAN);
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
}