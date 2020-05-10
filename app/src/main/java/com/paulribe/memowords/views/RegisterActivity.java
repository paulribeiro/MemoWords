package com.paulribe.memowords.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.paulribe.memowords.R;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

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
                        if(task.isSuccessful()){

                            final FirebaseDataHelper firebaseDataHelper;
                            if(mContext.getFirebaseDataHelper() == null) {
                                firebaseDataHelper = new FirebaseDataHelper();
                            } else {
                                firebaseDataHelper = mContext.getFirebaseDataHelper();
                            }
                            mContext.setCurrentUser(email);
                            firebaseDataHelper.setReferenceWords(LanguageEnum.GERMAN);
                            firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

                                @Override
                                public void dataIsLoaded(List<Word> w, List<String> keys) {
                                    mContext.setWords(w);
                                    mContext.setFirebaseDataHelper(firebaseDataHelper);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                                    mContext.setFirebaseDataHelper(firebaseDataHelper);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void dataIsDeleted() {

                                }
                            });
                            FirebaseDataHelper.addUser();
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
                        progressDialog.dismiss();
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
}
