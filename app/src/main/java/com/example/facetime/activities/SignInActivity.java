package com.example.facetime.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.facetime.R;

public class SignInActivity extends AppCompatActivity {

    private TextView textSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        initAction();
    }

    private void initView() {
        textSignUp = findViewById(R.id.textSignUp);
    }

    private void initAction() {
        textSignUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        });
    }

//    Test init User
//    private void initUser(){
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String, Object> user = new HashMap<>();
//        user.put("fist name", "Hai");
//        user.put("last name", "Dang");
//        user.put("email", "hai.dang@gmail.com");
//        database.collection("users")
//                .add(user)
//                .addOnSuccessListener(documentReference -> Toast.makeText(SignInActivity.this, "User inserted", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(SignInActivity.this, "Error adding user", Toast.LENGTH_SHORT).show());
//    }
}