package com.example.reels.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reels.MainActivity;
import com.example.reels.R;
import com.example.reels.databinding.ReelLayoutBinding;
import com.example.reels.models.reel_model;
import com.example.reels.profileActivity;

import java.util.ArrayList;

public class reel_adapter extends RecyclerView.Adapter<reel_adapter.viewHolder> {
    Context context;
    ArrayList<reel_model> arr ;
    Boolean isPlaying = true;

    public reel_adapter(Context context , ArrayList<reel_model> arr){
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reel_layout , parent , false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull reel_adapter.viewHolder holder, int position) {
        Log.d("RecyclerView", "Binding view for position: " + position);
        holder.binding.profileImageView.setImageResource(arr.get(position).getProfile());
        holder.binding.videoView.setVideoPath(arr.get(position).getUrl());
        holder.binding.userName.setText(arr.get(position).getName());

        holder.binding.videoView.setOnPreparedListener(MediaPlayer::start);

        holder.binding.videoView.setOnCompletionListener(MediaPlayer::start);

        holder.binding.videoView.setOnClickListener(view -> {
            if(isPlaying){
                holder.binding.videoView.pause();
                isPlaying = false;
            }else{
                holder.binding.videoView.resume();
                isPlaying = true;
            }
        });

        holder.binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context , profileActivity.class);
                context.startActivity(in);
            }
        });
        holder.binding.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context , profileActivity.class);
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        ReelLayoutBinding binding ;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReelLayoutBinding.bind(itemView);

        }
    }
}
