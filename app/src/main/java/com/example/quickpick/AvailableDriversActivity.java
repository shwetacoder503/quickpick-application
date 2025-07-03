package com.example.quickpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AvailableDriversActivity extends AppCompatActivity {

    LinearLayout driverListContainer;
    DatabaseReference dbRef;
    String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_drivers);

        driverListContainer = findViewById(R.id.driverListContainer);
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        destination = getIntent().getStringExtra("destination");

        fetchDriversByDestination(destination);
    }

    private void fetchDriversByDestination(String destination) {
        dbRef.orderByChild("userType").equalTo("driver").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        driverListContainer.removeAllViews();
                        boolean found = false;

                        for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                            String driverDest = driverSnapshot.child("destination").getValue(String.class);
                            String email = driverSnapshot.child("email").getValue(String.class);

                            if (driverDest != null && driverDest.equalsIgnoreCase(destination)) {
                                found = true;
                                String driverUid = driverSnapshot.getKey();

                                TextView driverView = new TextView(AvailableDriversActivity.this);
                                driverView.setText(email + " - " + driverDest);
                                driverView.setTextSize(18);
                                driverView.setPadding(10, 20, 10, 20);

                                driverView.setOnClickListener(v -> {
                                    Intent intent = new Intent(AvailableDriversActivity.this, DriverDetailsActivity.class);
                                    intent.putExtra("driverUid", driverUid);
                                    startActivity(intent);
                                });

                                driverListContainer.addView(driverView);
                            }
                        }

                        if (!found) {
                            TextView noDriverText = new TextView(AvailableDriversActivity.this);
                            noDriverText.setText("No drivers found for destination: " + destination);
                            noDriverText.setTextSize(18);
                            driverListContainer.addView(noDriverText);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AvailableDriversActivity.this, "Failed to fetch drivers", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
