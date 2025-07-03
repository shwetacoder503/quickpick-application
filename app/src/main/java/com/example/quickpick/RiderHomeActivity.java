package com.example.quickpick;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.io.IOException;
import java .util.Locale;

public class RiderHomeActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    TextView locationText;
    EditText etDestination;
    Button btnSubmitDestination;

    FusedLocationProviderClient locationClient;
    double currentLat, currentLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);

        locationText = findViewById(R.id.locationText);
        etDestination = findViewById(R.id.etDestination);
        btnSubmitDestination = findViewById(R.id.btnSubmitDestination);
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        fetchLocation();

        btnSubmitDestination.setOnClickListener(v -> {
            String destination = etDestination.getText().toString().trim();
            if (destination.isEmpty()) {
                Toast.makeText(this, "Please enter a destination", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ride requested from (" + currentLat + ", " + currentLng + ") to " + destination, Toast.LENGTH_LONG).show();
                // 🔜 Later: Save to Firebase or search for nearby drivers
                Intent intent = new Intent(RiderHomeActivity.this, AvailableDriversActivity.class);
                intent.putExtra("destination", etDestination.getText().toString().trim());
                startActivity(intent);

            }
        });
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        locationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLng = location.getLongitude();


                        // Geocoder logic placed inside the 'if' block
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(currentLat, currentLng, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                String fullAddress = address.getAddressLine(0);// Full address
                                locationText.setText("Current Location:\n" + fullAddress);

                                Toast.makeText(this, "Your Location: " + fullAddress, Toast.LENGTH_LONG).show();

                                // Optional: Also show it in a TextView instead of Toast
                                // locationText.setText("Your Location:\n" + fullAddress);
                            } else {
                                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Geocoder error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        locationText.setText("Location not available.");
                    }
                });

    }

    @Override

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation();
        } else {
            locationText.setText("Permission denied.");
        }
    }



}
