package com.example.reels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.databinding.ActivityLogInBinding;
import com.example.reels.models.logIn_model;

import java.util.ArrayList;

public class logInActivity extends AppCompatActivity {
    ActivityLogInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDBhelper database = new myDBhelper(this);
        Intent in = new Intent(logInActivity.this , MainActivity.class);

        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.etPhone.getText().toString();
                String Password = binding.etPassword.getText().toString();
                ArrayList<logIn_model> arr = database.fetchData();
                 boolean isValid = false;

               if(phone.isEmpty() || Password.isEmpty()){
                   Toast.makeText(logInActivity.this, "Phone number or password can't be empty", Toast.LENGTH_SHORT).show();
                   return;
               }

               for(int i = 0 ;i<arr.size();i++){
                   if(arr.get(i).getPhone().equals(phone) && arr.get(i).getPass().equals(Password)){
                       isValid = true;
                       break;
                   }
               }
               
               if(isValid){
                   SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
                   SharedPreferences.Editor editor = pref.edit();
                   editor.putBoolean("flag" , true);
                   editor.apply();
                   startActivity(in);
               }else{
                   Toast.makeText(logInActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}