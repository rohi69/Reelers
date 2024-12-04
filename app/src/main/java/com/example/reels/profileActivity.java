package com.example.reels;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.adapters.reel_adapter;
import com.example.reels.databinding.ActivityProfileBinding;
import com.example.reels.models.reel_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class profileActivity extends AppCompatActivity {
  ActivityProfileBinding binding;
  DatabaseReference databaseReference ;
  Uri videoUri , imageUri;
  String userName;
  ArrayList<reel_model>  arr = new ArrayList<>();
  reel_adapter reelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /*SharedPreferences preferences = getSharedPreferences("uploadedVideo", MODE_PRIVATE);
        String uriImage = preferences.getString("profile", null);
        Uri image = Uri.parse(uriImage);
        binding.profilePic.setImageURI(image);*/


        setSupportActionBar(binding.toolBar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolBar.setTitle("REELS");
            binding.toolBar.setSubtitle("Your Profile");
        }
        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("flag",true);
                editor.apply();
                Intent in = new Intent(profileActivity.this , firstPage.class);
                startActivity(in);
            }
        });



        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });


          SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
          String phoneNumber = pref.getString("phoneNumber" , null);
          databaseReference = FirebaseDatabase.getInstance().getReference("users");
          databaseReference.child(phoneNumber).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  userName =snapshot.child("username").getValue(String.class);
                  binding.userName.setText(userName);
                  binding.userDescription.setText(snapshot.child("userDescription").getValue(String.class));
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
                  Log.e("user name not found" , error.getMessage());
              }
          });


          binding.userDescription.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  EditText etUserDes = new EditText(profileActivity.this);
                  etUserDes.setText(binding.userDescription.getText().toString());
                  AlertDialog.Builder dialog = new AlertDialog.Builder(profileActivity.this);
                  dialog.setTitle("Enter about youself");
                  dialog.setView(etUserDes);
                  dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          String userDes = etUserDes.getText().toString();
                          binding.userDescription.setText(userDes);
                          //saving data in firebase
                          HashMap<String , String> aboutUser = new HashMap<>();
                          aboutUser.put("userDescription" , userDes);
                          databaseReference.child(phoneNumber).updateChildren((HashMap) aboutUser);
                      }
                  });
                  dialog.show();
              }
          });


          binding.uploapReel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                  intent.setType("video/*");
                  startActivityForResult(intent, 300);

              }
          });


    }




    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an option");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            } else if (which == 1) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 200);
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode ==100 && data != null){
                  imageUri = data.getData();
                binding.profilePic.setImageURI(imageUri);
            }else if (requestCode ==200 && data != null){
                imageUri = data.getData();
                binding.profilePic.setImageURI(imageUri);
            }else if(requestCode == 300 && data!= null){
                videoUri = data.getData();

                SharedPreferences preferences = getSharedPreferences("uploadedVideo" , MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", userName);
                editor.putInt("profile", imageUri.toString().hashCode());
                editor.putString("videoUrl" , videoUri.toString());
                editor.apply();

                arr.add(new reel_model(imageUri.toString().hashCode() , videoUri.toString() , userName));
                reelAdapter = new reel_adapter(profileActivity.this , arr);
                binding.viewPager.setAdapter(reelAdapter);

            }
        }

    }
}