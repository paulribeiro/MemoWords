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
import com.google.firebase.auth.FirebaseAuth;
import com.paulribe.memowords.R;
import com.paulribe.memowords.viewmodels.BaseViewModel;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView textPasswordForgotten;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup  = findViewById(R.id.textViewSignUp);
        textPasswordForgotten = findViewById(R.id.textPasswordForgotten);

        progressDialog = new ProgressDialog(this);

        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        textPasswordForgotten.setOnClickListener(this);
    }

    private void userLogin(){
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

        progressDialog.setMessage("Login, please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        BaseViewModel.setCurrentUser(task.getResult().getUser());
                        BaseViewModel.getFirebaseDataHelper().setReferenceUserConfig();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed",Toast.LENGTH_LONG).show();
                    }
                });
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
