package com.example.reels;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.databinding.ActivityDummyProfileBinding;

public class DummyProfile extends AppCompatActivity {
      ActivityDummyProfileBinding binding;
      boolean isPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDummyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent in =  getIntent();
        binding.userName.setText(in.getStringExtra("user"));
        binding.userDescription.setText("Hi I'm "+in.getStringExtra("user"));
        binding.videoView.setVideoPath(in.getStringExtra("videoUrl"));
        binding.videoView.start();
        isPlaying = true;
        binding.videoView.setOnClickListener(view -> {
            if(isPlaying){
                binding.videoView.pause();
                isPlaying = false;
            }else{
                binding.videoView.resume();
                isPlaying = true;
            }
        });

    }
}