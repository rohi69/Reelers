package com.example.reels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.databinding.ActivityLogInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class logInActivity extends AppCompatActivity {
    ActivityLogInBinding binding;
    DatabaseReference databaseReference;

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
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = binding.etPhone.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(logInActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(logInActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }

                verifyUserDetails(phoneNumber , password);

            }
        });
    }

    private void verifyUserDetails(String phoneNumber, String password) {
        databaseReference.child(phoneNumber).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String firebasePassword = snapshot.child("password").getValue(String.class);
                    if (firebasePassword != null && firebasePassword.equals(password)) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(logInActivity.this, MainActivity.class);
                        startActivity(in);

                        SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("flag" , true);
                        editor.apply();

                    } else {
                        Toast.makeText(this, "incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "user doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }else{
                Log.e("Firebase Error" , "error fetching user data"+task.getException());
            }
        });
    }
}

