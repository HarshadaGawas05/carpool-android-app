package com.project.carpool.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.project.carpool.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;
    private String sourceAddress;
    private String destinationAddress;
    private TextView estimatedTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Retrieve source and destination addresses from the intent
        sourceAddress = getIntent().getStringExtra("sourceAddress");
        destinationAddress = getIntent().getStringExtra("destinationAddress");

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        // Initialize TextView for estimated time
        estimatedTimeTextView = findViewById(R.id.estimatedTimeTextView);

        // Initialize payment button and set click listener
        Button paymentButton = findViewById(R.id.buttonPayment);
        paymentButton.setOnClickListener(v -> {
            // Open the UPI link
            String upiLink = "https://gpay.app.goo.gl/kHXsJi";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(upiLink));
            startActivity(Intent.createChooser(intent, "Pay with"));
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            // Convert source address to coordinates
            LatLng sourceLatLng = getLocationFromAddress(sourceAddress);
            if (sourceLatLng != null) {
                mMap.addMarker(new MarkerOptions().position(sourceLatLng).title("Source"));
            }

            // Convert destination address to coordinates
            LatLng destLatLng = getLocationFromAddress(destinationAddress);
            if (destLatLng != null) {
                mMap.addMarker(new MarkerOptions().position(destLatLng).title("Destination"));
            }

            // Draw route between source and destination
            drawRoute(sourceLatLng, destLatLng);

            // Zoom camera to fit both source and destination markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(sourceLatLng);
            builder.include(destLatLng);
            LatLngBounds bounds = builder.build();
            int padding = 400; // Padding around the markers
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cameraUpdate);

            // Get estimated time and display it
            calculateAndDisplayEstimatedTime(sourceLatLng, destLatLng);
        }
    }

    // Method to draw route between source and destination
    private void drawRoute(LatLng sourceLatLng, LatLng destLatLng) {
        if (sourceLatLng != null && destLatLng != null) {
            // Define polyline options
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.width(10);
            polylineOptions.color(Color.BLACK);

            // Add source and destination to the polyline
            polylineOptions.add(sourceLatLng, destLatLng);

            // Add polyline to map
            mMap.addPolyline(polylineOptions);
        }
    }

    // Method to get LatLng from address using Geocoding
    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(strAddress, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to calculate and display estimated time
    private void calculateAndDisplayEstimatedTime(LatLng sourceLatLng, LatLng destLatLng) {
        // Calculate distance between source and destination
        float[] results = new float[1];
        Location.distanceBetween(
                sourceLatLng.latitude, sourceLatLng.longitude,
                destLatLng.latitude, destLatLng.longitude,
                results);

        double distanceInMeters = results[0];
        double distanceInKm = distanceInMeters / 1000;

        // Assuming average speed of 30 km/h
        double averageSpeedKmPerHour = 30;
        double estimatedTimeHours = distanceInKm / averageSpeedKmPerHour;
        long estimatedTimeMinutes = (long) (estimatedTimeHours * 60);

        // Calculate hours and minutes
        long hours = estimatedTimeMinutes / 60;
        long minutes = estimatedTimeMinutes % 60;

        // Format the estimated time
        String estimatedTime;
        if (hours > 0) {
            estimatedTime = "Estimated Time: " + hours + " hrs " + minutes + " mins";
        } else {
            estimatedTime = "Estimated Time: " + minutes + " mins";
        }

        // Display the estimated time
        estimatedTimeTextView.setText(estimatedTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}