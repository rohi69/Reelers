package com.example.reels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reels.databinding.ActivitySignUpBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class signUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String verificationId;

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


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users"); // Point to "users" node in database

        // Send OTP button
        binding.sendOtp.setOnClickListener(view -> {
            String phoneNumber = binding.etPhone.getText().toString();

            if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
                Toast.makeText(signUpActivity.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            sendOtp(phoneNumber);
        });

        // Validate OTP button
        binding.validateOtpBtn.setOnClickListener(view -> {
            String userOtp = binding.etOtp.getText().toString().trim();

            if (userOtp.isEmpty() || userOtp.length() < 6) {
                Toast.makeText(signUpActivity.this, "Enter a valid OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            validateOtp(userOtp);
        });
    }




    // Method to send OTP
    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91" + phoneNumber) // Ensure the phone number includes the country code
                .setTimeout(60L, TimeUnit.SECONDS)  // Timeout duration for OTP
                .setActivity(this)                 // Current activity
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Auto-verification completed (OTP may be auto-filled)
                        Log.d("Firebase", "Verification completed: " + credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // Verification failed
                        Log.e("Firebase", "Verification failed: " + e.getMessage());
                        Toast.makeText(signUpActivity.this, "Failed to send OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // OTP sent successfully
                        Log.d("Firebase", "OTP sent: " + verificationId);
                        Toast.makeText(signUpActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                        signUpActivity.this.verificationId = verificationId; // Save the verification ID
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Method to validate OTP
    private void validateOtp(String userOtp) {
        if (verificationId == null) {
            Toast.makeText(this, "Verification ID is null. Send OTP first.", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOtp);
        signInWithPhoneAuthCredential(credential);
    }

    // Sign in with the verified credential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Save the phone number to Firebase Realtime Database
                String phoneNumber = binding.etPhone.getText().toString().trim();

                // Navigate to UserActivity after successful OTP verification
                Toast.makeText(signUpActivity.this, "OTP Verified! Redirecting...", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(signUpActivity.this, userSignUpActivity.class);
                startActivity(intent);

                SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("flag" , true);
                editor.putString("phoneNumber" , phoneNumber);
                editor.apply();

            } else {

                Toast.makeText(signUpActivity.this, "Invalid OTP. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

