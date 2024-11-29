package com.example.reels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        VideoView videoView = findViewById(R.id.videoView);
        String vPath = "android.resource://"+getPackageName()+"/raw/intro";
        Uri videoUri = Uri.parse(vPath);
        videoView.setVideoURI(videoUri);
        videoView.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
                boolean bool = pref.getBoolean("flag" , false);
                Intent in ;

                if(bool){
                    in  = new Intent(splashActivity.this , MainActivity.class);
                }else{
                    in  = new Intent(splashActivity.this , firstPage.class);
                }
               startActivity(in);
                finish();
            }
        },3000);

    }
}