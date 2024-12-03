package com.example.flytix;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    TextView loginTxt;
    EditText unameTxt,emailTxt,passTxt,conpassTxt;

    DatabaseReference userDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.btnRegister);
        loginTxt = findViewById(R.id.txtLogin);
        unameTxt = findViewById(R.id.txtUname);
        emailTxt = findViewById(R.id.txtEmail);
        passTxt = findViewById(R.id.txtPass);
        conpassTxt = findViewById(R.id.txtConfPass);

        FirebaseDatabase database=FirebaseDatabase.getInstance("https://gofly-72506-default-rtdb.firebaseio.com/");

        userDbRef = database.getReference("users");


        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = unameTxt.getText().toString().trim();
                String email = emailTxt.getText().toString().trim();
                String password = passTxt.getText().toString().trim();
                String confirm = conpassTxt.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isOnline()) {
                    Toast.makeText(RegisterActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "Password and Confirm password didn't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(RegisterActivity.this, "Password must contain at least 8 characters, a letter, a digit, and a special symbol", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hash the password before storing (for simplicity, we're not doing this here)
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Username", username);
                hashMap.put("email", email);
                hashMap.put("password", password);

                // Push user data to Firebase
                userDbRef.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Failed to register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

        private boolean isOnline() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        public boolean isValidPassword(String password) {
            int letterCount = 0;
            int digitCount = 0;
            int specialCount = 0;

            if (password.length() < 8) {
                return false;
            } else {
                for (int i = 0; i < password.length(); i++) {
                    char c = password.charAt(i);
                    if (Character.isLetter(c)) {
                        letterCount++;
                    } else if (Character.isDigit(c)) {
                        digitCount++;
                    } else if (isSpecialCharacter(c)) {
                        specialCount++;
                    }
                }

                return (letterCount > 0 && digitCount > 0 && specialCount > 0);
            }
        }

        private static boolean isSpecialCharacter(char c) {
            return (c >= 33 && c <= 46) || c == 64;
        }

        public boolean isValidEmail(String email) {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            return Pattern.compile(emailPattern).matcher(email).matches();
        }

}