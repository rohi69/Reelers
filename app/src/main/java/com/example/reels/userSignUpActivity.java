package com.example.reels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.databinding.ActivityUserSignUpBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class userSignUpActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    String phoneNumber;
    ActivityUserSignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityUserSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
        phoneNumber = pref.getString("phoneNumber" , null);

        // Set up button click for saving username and password
        binding.signUpBtn.setOnClickListener(view -> {
            String username = binding.etUserName.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            // Validate inputs
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save user details to Firebase
            saveUserDetails(username, password);
        });
    }

    // Method to save user details to Firebase
    private void saveUserDetails(String username, String password) {
        // Create a HashMap to store user details
        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put("username", username);
        userDetails.put("password", password);

        // Store user details under the phone number in Firebase
        databaseReference.child(phoneNumber).setValue(userDetails).addOnCompleteListener(task ->{
            if (task.isSuccessful()) {
                Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to the UserActivity
                Intent intent = new Intent(userSignUpActivity.this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Failed to save details. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
