package com.example.facetime.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facetime.R;
import com.example.facetime.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textFistChar, textUserName, textEmail;
        ImageView imageAudio, imageVideo;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textFistChar = itemView.findViewById(R.id.textFistChar);
            textUserName = itemView.findViewById(R.id.textUserName);
            textEmail = itemView.findViewById(R.id.textEmail);
            imageAudio = itemView.findViewById(R.id.imageAudio);
            imageVideo = itemView.findViewById(R.id.imageVideoCall);
        }

        void setUserData(User user){
            textFistChar.setText(user.fistName.substring(0,1));
            textUserName.setText(String.format("%s %s", user.fistName, user.lastName));
            textEmail.setText(user.email);
        }
    }
}
