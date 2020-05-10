package com.paulribe.memowords.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.paulribe.memowords.R;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordForgottenActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private EditText forgottenEmailEditText;
    private Button sendForgottenEmailButton;
    private TextView backToLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgotten);

        firebaseAuth = FirebaseAuth.getInstance();

        forgottenEmailEditText = findViewById(R.id.forgottenEmailEditText);
        sendForgottenEmailButton = findViewById(R.id.sendForgottenEmailButton);
        backToLoginTextView  = findViewById(R.id.backToLoginTextView);

        sendForgottenEmailButton.setOnClickListener(this);
        backToLoginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == sendForgottenEmailButton) {
            firebaseAuth.sendPasswordResetEmail(forgottenEmailEditText.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PasswordForgottenActivity.this, "Email sent",Toast.LENGTH_LONG).show();
                            backToLogin();
                        } else {
                            Toast.makeText(PasswordForgottenActivity.this, "Can't send email",Toast.LENGTH_LONG).show();
                        }
                    });
        } else if (view == backToLoginTextView) {
            backToLogin();
        }

    }

    private void backToLogin() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
