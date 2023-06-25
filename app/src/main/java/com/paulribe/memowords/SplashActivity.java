package com.paulribe.memowords;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.paulribe.memowords.authentication.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCenter.start(getApplication(), "ef72f7fd-0573-4e30-a46e-2994690178ee",
                Analytics.class, Crashes.class);

        initDataBinding();

        FirebaseUser currentUser = splashViewModel.getCurrentUser();
        if(currentUser == null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void initDataBinding() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        splashViewModel.init();
    }
}