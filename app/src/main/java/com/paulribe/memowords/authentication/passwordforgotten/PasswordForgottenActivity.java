package com.paulribe.memowords.authentication.passwordforgotten;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paulribe.memowords.R;
import com.paulribe.memowords.authentication.login.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class PasswordForgottenActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText forgottenEmailEditText;
    private Button sendForgottenEmailButton;
    private TextView backToLoginTextView;
    private ProgressDialog progressDialog;
    private PasswordForgottenViewModel passwordForgottenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgotten);

        initDataBinding();

        progressDialog = new ProgressDialog(this);

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
        if(isLoading) {
            progressDialog.setMessage(getString(R.string.sending_reset_email));
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    public void onIsPasswordResetSuccessfulChanged(Boolean isPasswordResetSuccessful) {
        if(isPasswordResetSuccessful) {
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
