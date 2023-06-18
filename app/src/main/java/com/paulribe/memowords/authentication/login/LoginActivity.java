package com.paulribe.memowords.authentication.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paulribe.memowords.R;
import com.paulribe.memowords.MainActivity;
import com.paulribe.memowords.authentication.passwordforgotten.PasswordForgottenActivity;
import com.paulribe.memowords.authentication.register.RegisterActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView textPasswordForgotten;
    private ProgressDialog progressDialog;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initDataBinding();

        if(loginViewModel.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        defineViews();

        progressDialog = new ProgressDialog(this);

        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        textPasswordForgotten.setOnClickListener(this);
    }

    private void defineViews() {
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup  = findViewById(R.id.textViewSignUp);
        textPasswordForgotten = findViewById(R.id.textPasswordForgotten);
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, R.string.please_enter_email,Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, R.string.please_enter_password,Toast.LENGTH_LONG).show();
            return;
        }

        loginViewModel.loginByEmailAndPassword(email, password);
    }

    private void initDataBinding() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.init();
        setUpChangeValueListener();
    }

    private void setUpChangeValueListener() {
        loginViewModel.getIsLoading().observe(this, this::onIsLoadingChanged);
        loginViewModel.getIsLoginSuccessful().observe(this, this::onIsLoginSuccessfulChanged);
    }

    public void onIsLoadingChanged(Boolean isLoading) {
        if(isLoading) {
            progressDialog.setMessage(getString(R.string.login_please_wait));
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    public void onIsLoginSuccessfulChanged(Boolean isLoginSuccessful) {
        if(isLoginSuccessful) {
            openMainActivity();
        } else {
            displayLoginFailed();
        }
    }

    public void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void displayLoginFailed() {
        Toast.makeText(LoginActivity.this, R.string.login_failed,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){
            userLogin();
        } else if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (view == textPasswordForgotten) {
            finish();
            startActivity(new Intent(this, PasswordForgottenActivity.class));
        }
    }
}
