package com.example.reels;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.databinding.ActivitySignUpBinding;

import java.util.Random;

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

        binding.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int otp = random.nextInt(9000)+1000;
                String phone = binding.etPhone.getText().toString();
                if(ContextCompat.checkSelfPermission(signUpActivity.this , Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    sendOtp(phone , otp);
                }else{
                    ActivityCompat.requestPermissions(signUpActivity.this , new String[]{Manifest.permission.SEND_SMS},otp);
                }
                 if (binding.etOtp != null){
                      otp = Integer.parseInt(binding.etOtp.getText().toString());
                      binding.etPassword.setVisibility(View.VISIBLE);
                      binding.signUpBtn.setVisibility(View.VISIBLE);
                 }
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.etPhone.getText().toString();
                String Password = binding.etPassword.getText().toString();
                 if(database.emailExists(phone)) {
                     Toast.makeText(signUpActivity.this, "Email is already is use", Toast.LENGTH_SHORT).show();
                 }else{
                     database.addData(phone, Password);
                     SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
                     SharedPreferences.Editor editor = pref.edit();
                     editor.putBoolean("flag" , true);
                     editor.apply();
                     startActivity(in);
                 }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendOtp(binding.etPhone.getText().toString(), requestCode);
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendOtp(String phone , int otp){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("91"+phone , null , "Your otp for sign up :"+otp , null , null );
    }


}