package com.example.facetime.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetime.R;
import com.example.facetime.adapters.UsersAdapter;
import com.example.facetime.models.User;
import com.example.facetime.utilities.Constants;
import com.example.facetime.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;

    private List<User> users;

    private UsersAdapter usersAdapter;

    private TextView textErrorMessage;

    private ProgressBar usersProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceManager = new PreferenceManager(getApplicationContext());
        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText(String.format("%s %s",
                preferenceManager.getString(Constants.KEY_FIST_NAME),
                preferenceManager.getString(Constants.KEY_LAST_NAME)
        ));

        findViewById(R.id.textSignOut).setOnClickListener(v -> {
            signOut();
        });

        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getToken() != null) {
                sendFCMTokenToDatabase(task.getResult().getToken());
            }
        });
        RecyclerView userRecyclerView = findViewById(R.id.userRecyclerView);
        textErrorMessage = findViewById(R.id.textErrorMessage);
        usersProgressBar = findViewById(R.id.usersProgressBar);
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users);
        userRecyclerView.setAdapter(usersAdapter);

        getUser();
    }

    private void getUser() {
        usersProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER).get()
                .addOnCompleteListener(task -> {
                    usersProgressBar.setVisibility(View.GONE);
                    String myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (myUserId.equals(documentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.fistName = documentSnapshot.getString(Constants.KEY_FIST_NAME);
                            user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            textErrorMessage.setText(String.format("%s", "No user available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textErrorMessage.setText(String.format("%s", "No user available"));
                        textErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendFCMTokenToDatabase(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USER).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, "Unable to send token", Toast.LENGTH_SHORT).show();
        });
    }

    private void signOut() {
        Toast.makeText(MainActivity.this, "Sign Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USER).document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clearPreference();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to Sign out", Toast.LENGTH_SHORT).show();
                });
    }
}