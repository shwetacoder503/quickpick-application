package com.example.quickpick;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class DriverDetailsActivity extends AppCompatActivity {

    TextView detailsTextView;
    Button confirmButton;
    String driverUid;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        detailsTextView = findViewById(R.id.driverDetailsText);
        confirmButton = findViewById(R.id.confirmButton);

        driverUid = getIntent().getStringExtra("driverUid");
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        if (driverUid != null && !driverUid.isEmpty()) {
            loadDriverDetails(driverUid);
        } else {
            Toast.makeText(this, "Driver not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        confirmButton.setOnClickListener(v -> {
            Toast.makeText(this, "Ride confirmed with this driver!", Toast.LENGTH_SHORT).show();
            // You can add further logic like saving confirmation to Firebase here
        });
    }

    private void loadDriverDetails(String uid) {
        dbRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = snapshot.child("email").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String contact = snapshot.child("contact").getValue(String.class);
                String vehicle = snapshot.child("vehicle").getValue(String.class);
                String destination = snapshot.child("destination").getValue(String.class);

                StringBuilder details = new StringBuilder();
                details.append("Email: ").append(email).append("\n");
                details.append("Name: ").append(name).append("\n");
                details.append("Contact: ").append(contact).append("\n");
                details.append("Vehicle No: ").append(vehicle).append("\n");
                details.append("Destination: ").append(destination).append("\n");

                detailsTextView.setText(details.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverDetailsActivity.this, "Failed to load driver details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
