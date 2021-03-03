package com.example.facetime.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.facetime.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.imageBack).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.textSignIn).setOnClickListener(v -> {
            onBackPressed();
        });
    }
}