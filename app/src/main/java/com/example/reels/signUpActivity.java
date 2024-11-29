package com.example.reels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.databinding.ActivitySignUpBinding;

public class signUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDBhelper database = new myDBhelper(this);
        Intent in = new Intent(signUpActivity.this , MainActivity.class);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = binding.etGmail.getText().toString();
                String Password = binding.etPassword.getText().toString();
                 if(database.emailExists(Email)) {
                     Toast.makeText(signUpActivity.this, "Email is already is use", Toast.LENGTH_SHORT).show();
                 }else{
                     database.addData(Email, Password);
                     SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
                     SharedPreferences.Editor editor = pref.edit();
                     editor.putBoolean("flag" , true);
                     editor.apply();
                     startActivity(in);
                 }
            }
        });
    }

}