package com.example.reels;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.adapters.reel_adapter;
import com.example.reels.databinding.ActivityMainBinding;
import com.example.reels.models.reel_model;
import com.google.firebase.database.DatabaseKt;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<reel_model> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        arr.add(new reel_model(R.drawable.profile ,"android.resource://"+getPackageName()+"/raw/vid1" , "ROHIT"));
        arr.add(new reel_model(R.drawable.boy ,"android.resource://"+getPackageName()+"/raw/vid2" , "Rahul"));
        arr.add(new reel_model(R.drawable.man ,"android.resource://"+getPackageName()+"/raw/vid3" , "Prateek"));
        arr.add(new reel_model(R.drawable.woman ,"android.resource://"+getPackageName()+"/raw/vid4" , "Shubham"));
        arr.add(new reel_model(R.drawable.profile ,"android.resource://"+getPackageName()+"/raw/vid5" , "ROHIT"));
        arr.add(new reel_model(R.drawable.profile ,"android.resource://"+getPackageName()+"/raw/vid1" , "ROHIT"));
        arr.add(new reel_model(R.drawable.boy ,"android.resource://"+getPackageName()+"/raw/vid2" , "Rahul"));
        arr.add(new reel_model(R.drawable.man ,"android.resource://"+getPackageName()+"/raw/vid3" , "Prateek"));
        arr.add(new reel_model(R.drawable.woman ,"android.resource://"+getPackageName()+"/raw/vid4" , "Shubham"));
        arr.add(new reel_model(R.drawable.profile ,"android.resource://"+getPackageName()+"/raw/vid5" , "ROHIT"));

        reel_adapter reelAdapter = new reel_adapter(this , arr);
        binding.viewPager.setAdapter(reelAdapter);



      DatabaseReference userRef =  FirebaseDatabase.getInstance().getReference("Users");
      userRef.push().setValue("Rohit");
      userRef.push().setValue("Prateek");
      userRef.push().setValue("Rahul");

      String id  = userRef.push().getKey();
      userRef.child("Rohit").setValue("topwal");




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}