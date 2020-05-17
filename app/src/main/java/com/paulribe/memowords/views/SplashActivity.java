package com.paulribe.memowords.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.restclient.FirebaseDataHelper;
import com.paulribe.memowords.viewmodels.BaseViewModel;

public class SplashActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseViewModel.setFirebaseDataHelper(new FirebaseDataHelper());
        // firebaseDataHelper.setReferenceWords(LanguageEnum.GERMAN);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        BaseViewModel.setCurrentUser(currentUser);
        if(currentUser == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}