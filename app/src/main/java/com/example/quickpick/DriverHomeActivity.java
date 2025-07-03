package com.example.quickpick;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DriverHomeActivity extends AppCompatActivity {

    EditText etName, etContact, etVehicle;
    Button btnSave;

    FirebaseAuth auth;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        etName = findViewById(R.id.etName);
        etContact = findViewById(R.id.etContact);
        etVehicle = findViewById(R.id.etVehicle);
        btnSave = findViewById(R.id.btnSave);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String contact = etContact.getText().toString().trim();
            String vehicle = etVehicle.getText().toString().trim();

            if (name.isEmpty() || contact.isEmpty() || vehicle.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = auth.getCurrentUser().getUid();
            Map<String, Object> driverDetails = new HashMap<>();
            driverDetails.put("name", name);
            driverDetails.put("contact", contact);
            driverDetails.put("vehicle", vehicle);

            dbRef.child(uid).updateChildren(driverDetails)
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
