package com.paulribe.memowords.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.viewmodels.SplashViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataBinding();

        FirebaseUser currentUser = splashViewModel.getCurrentUser();
        if(currentUser == null) {
            new Handler().postDelayed(() -> {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            new Handler().postDelayed(() -> {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void initDataBinding() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        splashViewModel.init();
    }
}