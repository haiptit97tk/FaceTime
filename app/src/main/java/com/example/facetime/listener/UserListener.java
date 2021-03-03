package com.example.facetime.listener;

import com.example.facetime.models.User;

public interface UserListener {
    void initiateAudioMeeting(User user);
    void initiateVideoMeeting(User user);
}
