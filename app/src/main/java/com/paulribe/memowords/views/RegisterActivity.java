package com.paulribe.memowords.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.paulribe.memowords.R;
import com.paulribe.memowords.countrypicker.CountryAdapter;
import com.paulribe.memowords.countrypicker.CountryItem;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private ArrayList<CountryItem> mCountryList;

    private LanguageEnum nativeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewSignin = findViewById(R.id.textViewSignin);
        buttonSignup = findViewById(R.id.buttonSignup);

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

        initPossibleLanguages();
        Spinner spinnerCountries = findViewById(R.id.flags_spinner);
        CountryAdapter mAdapter = new CountryAdapter(this, mCountryList);
        spinnerCountries.setAdapter(mAdapter);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                LanguageEnum nativeLanguageSelected = clickedItem.getCountryName();
                if(nativeLanguageSelected != null && nativeLanguageSelected != LanguageEnum.NONE) {
                    nativeLanguage = nativeLanguageSelected;
                }
                checkRequiredFieldsForButtonSignup();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initPossibleLanguages() {
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountryItem(LanguageEnum.NONE, 0));
        mCountryList.add(new CountryItem(LanguageEnum.GERMAN, R.mipmap.de_flag));
        mCountryList.add(new CountryItem(LanguageEnum.SPANISH, R.mipmap.es_flag));
        mCountryList.add(new CountryItem(LanguageEnum.FRENCH, R.mipmap.fr_flag));
        mCountryList.add(new CountryItem(LanguageEnum.ENGLISH, R.mipmap.gb_flag));
        mCountryList.add(new CountryItem(LanguageEnum.PORTUGUESE, R.mipmap.pt_flag));
    }

    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            BaseViewModel.setCurrentUser(task.getResult().getUser());
                            BaseViewModel.getFirebaseDataHelper().setReferenceUserConfig();
                            BaseViewModel.getFirebaseDataHelper().addUser(nativeLanguage);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this,"This user already exists",Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegisterActivity.this,"Wrong username format",Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(RegisterActivity.this,"Your password is not strong enough",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
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
        if (!editTextEmail.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty() && nativeLanguage != null) {
            buttonSignup.setEnabled(true);
        } else {
            buttonSignup.setEnabled(false);
        }
    }
}
