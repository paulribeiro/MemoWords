package com.paulribe.memowords.authentication.passwordforgotten;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.paulribe.memowords.LoadingDialog;
import com.paulribe.memowords.R;
import com.paulribe.memowords.authentication.login.LoginActivity;

public class PasswordForgottenActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText forgottenEmailEditText;
    private Button sendForgottenEmailButton;
    private TextView backToLoginTextView;
    private LoadingDialog loadingDialog;
    private PasswordForgottenViewModel passwordForgottenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgotten);

        initDataBinding();

        loadingDialog = new LoadingDialog(this);

        forgottenEmailEditText = findViewById(R.id.forgottenEmailEditText);
        sendForgottenEmailButton = findViewById(R.id.sendForgottenEmailButton);
        backToLoginTextView  = findViewById(R.id.backToLoginTextView);

        sendForgottenEmailButton.setOnClickListener(this);
        backToLoginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == sendForgottenEmailButton) {
            passwordForgottenViewModel.sendResetPasswordEmail(forgottenEmailEditText.getText().toString());
        } else if (view == backToLoginTextView) {
            backToLogin();
        }

    }

    private void initDataBinding() {
        passwordForgottenViewModel = new ViewModelProvider(this).get(PasswordForgottenViewModel.class);
        passwordForgottenViewModel.init();
        setUpChangeValueListener();
    }

    private void setUpChangeValueListener() {
        passwordForgottenViewModel.getIsLoading().observe(this, this::onIsLoadingChanged);
        passwordForgottenViewModel.getIsPasswordResetSuccessful().observe(this, this::onIsPasswordResetSuccessfulChanged);
    }

    public void onIsLoadingChanged(Boolean isLoading) {
        if(Boolean.TRUE.equals(isLoading)) {
            loadingDialog.startLoadingDialog(getString(R.string.sending_reset_email));
        } else {
            loadingDialog.dismissDialog();
        }
    }

    public void onIsPasswordResetSuccessfulChanged(Boolean isPasswordResetSuccessful) {
        if(Boolean.TRUE.equals(isPasswordResetSuccessful)) {
            Toast.makeText(PasswordForgottenActivity.this, R.string.email_sent,Toast.LENGTH_LONG).show();
            backToLogin();
        } else {
            Toast.makeText(PasswordForgottenActivity.this, R.string.email_not_sent,Toast.LENGTH_LONG).show();
        }
    }

    private void backToLogin() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
