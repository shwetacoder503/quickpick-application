package com.example.quickpick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etDriverDestination;
    RadioGroup roleGroup;
    Button btnRegister;
    TextView textview;
    FirebaseAuth auth;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etDriverDestination = findViewById(R.id.etDriverDestination);
        roleGroup = findViewById(R.id.roleGroup);
        btnRegister = findViewById(R.id.btnRegister);
        textview = findViewById(R.id.textview1);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        // Show/hide destination input based on user type
        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.driverBtn) {
                etDriverDestination.setVisibility(View.VISIBLE);
            } else {
                etDriverDestination.setVisibility(View.GONE);
            }
        });

        textview.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            int selectedId = roleGroup.getCheckedRadioButtonId();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedId == -1) {
                Toast.makeText(this, "Select role: Rider or Driver", Toast.LENGTH_SHORT).show();
                return;
            }

            String userType = ((RadioButton) findViewById(selectedId)).getText().toString().toLowerCase();
            String destination = etDriverDestination.getText().toString().trim();

            if (userType.equals("driver") && destination.isEmpty()) {
                Toast.makeText(this, "Please enter destination", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        String uid = auth.getCurrentUser().getUid();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        userData.put("userType", userType);
                        if (userType.equals("driver")) {
                            userData.put("destination", destination);
                        }
                        dbRef.child(uid).setValue(userData);
                        Toast.makeText(this, "Registered as " + userType, Toast.LENGTH_SHORT).show();
                        finish(); // Go to login
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
