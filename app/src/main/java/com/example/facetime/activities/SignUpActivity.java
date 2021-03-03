package com.example.facetime.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.facetime.R;
import com.example.facetime.utilities.Constants;
import com.example.facetime.utilities.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputFistName, inputLastName, inputEmail, inputPassword, inputConfirmPassword;

    private MaterialButton buttonSignUp;

    private ProgressBar signUpProgressBar;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        preferenceManager = new PreferenceManager(getApplicationContext());
        findViewById(R.id.imageBack).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.textSignIn).setOnClickListener(v -> {
            onBackPressed();
        });
        initAction();
    }

    private void initView() {
        inputFistName = findViewById(R.id.inputFistName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);
    }

    private void initAction() {
        buttonSignUp.setOnClickListener(v -> {
            if (inputFistName.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter fist name", Toast.LENGTH_SHORT).show();
            } else if (inputLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if (inputEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter email    ", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {
                Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            } else if (inputPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter password    ", Toast.LENGTH_SHORT).show();
            } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Confirm your password   ", Toast.LENGTH_SHORT).show();
            } else if (!inputPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim())) {
                Toast.makeText(SignUpActivity.this, "Password & Confirm Password must be the same", Toast.LENGTH_SHORT).show();
            } else {
                signUp();
            }
        });
    }

    private void signUp() {
        buttonSignUp.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_FIST_NAME, inputFistName.getText().toString().trim());
        user.put(Constants.KEY_LAST_NAME, inputLastName.getText().toString().trim());
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString().trim());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString().trim());
        database.collection(Constants.KEY_COLLECTION_USER).add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_FIST_NAME, inputFistName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_LAST_NAME, inputLastName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString().trim());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    signUpProgressBar.setVisibility(View.INVISIBLE);
                    buttonSignUp.setVisibility(View.VISIBLE);
                    Toast.makeText(SignUpActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}