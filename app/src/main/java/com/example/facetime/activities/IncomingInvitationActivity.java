package com.example.facetime.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetime.R;
import com.example.facetime.network.ApiClient;
import com.example.facetime.network.ApiService;
import com.example.facetime.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingInvitationActivity extends AppCompatActivity {

    private TextView textUserName, textEmail, textFistChar;

    private ImageView imageMeetingType, imageAcceptInvitation, imageRejectInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomming_invitation);
        imageMeetingType = findViewById(R.id.imageMeetingType);
        textFistChar = findViewById(R.id.textFistChar);
        textUserName = findViewById(R.id.textUserName);
        textEmail = findViewById(R.id.textEmail);
        imageAcceptInvitation = findViewById(R.id.imageAcceptInvitation);
        imageRejectInvitation = findViewById(R.id.imageRejectInvitation);
        String meetingType = getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);
        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imageMeetingType.setImageResource(R.drawable.ic_video_call);
            } else {
                imageMeetingType.setImageResource(R.drawable.ic_audio);
            }
        }
        String firstName = getIntent().getStringExtra(Constants.KEY_FIST_NAME);
        if (firstName != null) {
            textFistChar.setText(firstName.substring(0, 1));
        }
        textUserName.setText(String.format("%s %s", firstName, getIntent().getStringExtra(Constants.KEY_LAST_NAME)));
        textEmail.setText(getIntent().getStringExtra(Constants.KEY_EMAIL));

        imageAcceptInvitation.setOnClickListener(v -> {
            sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_ACCEPTED, getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN));
        });

        imageRejectInvitation.setOnClickListener(v -> {
            sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_REJECTED, getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN));
        });
    }

    private void sendInvitationResponse(String type, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);
            sendRemoteMessage(body.toString(), type);
        } catch (Exception e) {
            Toast.makeText(IncomingInvitationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(Constants.getRemoteMessageHeaders(), remoteMessageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                        Toast.makeText(IncomingInvitationActivity.this, "Invitation accepted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IncomingInvitationActivity.this, "Invitation rejected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IncomingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(IncomingInvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}