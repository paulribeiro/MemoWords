package com.paulribe.memowords.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.paulribe.memowords.R;
import com.paulribe.memowords.countrypicker.CountryAdapter;
import com.paulribe.memowords.countrypicker.CountryItem;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.viewmodels.RegisterViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initDataBinding();

        if(registerViewModel.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewSignin = findViewById(R.id.textViewSignin);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonSignup.setEnabled(false);
        progressDialog = new ProgressDialog(this);
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                checkRequiredFieldsForButtonSignup();
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                checkRequiredFieldsForButtonSignup();
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });


        Spinner spinnerCountries = findViewById(R.id.flags_spinner);
        CountryAdapter mAdapter = new CountryAdapter(this, registerViewModel.getmCountryList());
        spinnerCountries.setAdapter(mAdapter);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                LanguageEnum nativeLanguageSelected = clickedItem.getCountryName();
                if(nativeLanguageSelected != null && nativeLanguageSelected != LanguageEnum.NONE) {
                    registerViewModel.setNativeLanguage(nativeLanguageSelected);
                }
                checkRequiredFieldsForButtonSignup();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, R.string.please_enter_email, Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, R.string.please_enter_password, Toast.LENGTH_LONG).show();
            return;
        }

        registerViewModel.registerUser(email, password);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignup){
            registerUser();
        }
        if(view == textViewSignin){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void checkRequiredFieldsForButtonSignup() {
        if (!editTextEmail.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty() &&
                registerViewModel.getNativeLanguage() != null && registerViewModel.getNativeLanguage() != LanguageEnum.NONE) {
            buttonSignup.setEnabled(true);
        } else {
            buttonSignup.setEnabled(false);
        }
    }

    private void initDataBinding() {
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.init();
        setUpChangeValueListener();
    }

    private void setUpChangeValueListener() {
        registerViewModel.getIsLoading().observe(this, this::onIsLoadingChanged);
        registerViewModel.getIsRegisterSuccessful().observe(this, this::onIsRegisterSuccessfulChanged);
    }

    public void onIsLoadingChanged(Boolean isLoading) {
        if(isLoading) {
            progressDialog.setMessage(getString(R.string.registering));
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    public void onIsRegisterSuccessfulChanged(Boolean isRegistrationSuccessful) {
        if(isRegistrationSuccessful) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (registerViewModel.getTaskRegistrationResultException() instanceof FirebaseAuthUserCollisionException) {
                Toast.makeText(RegisterActivity.this, R.string.user_already_exists,Toast.LENGTH_LONG).show();
            } else if (registerViewModel.getTaskRegistrationResultException() instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(RegisterActivity.this, R.string.wrong_username_format,Toast.LENGTH_LONG).show();
            } else if (registerViewModel.getTaskRegistrationResultException() instanceof FirebaseAuthWeakPasswordException) {
                Toast.makeText(RegisterActivity.this, R.string.password_not_strong_enough,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegisterActivity.this, R.string.registration_error,Toast.LENGTH_LONG).show();
            }
        }
    }
}
