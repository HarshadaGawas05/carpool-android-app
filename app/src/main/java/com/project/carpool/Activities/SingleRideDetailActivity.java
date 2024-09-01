package com.project.carpool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.carpool.Model.Model;
import com.project.carpool.R;

public class SingleRideDetailActivity extends AppCompatActivity {

    private DatabaseReference passengerDetailsRef;
    private LinearLayout passengerDetailsLayout;
    private Button confirmRideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_ride_detail);

        // Initialize Firebase database reference
        passengerDetailsRef = FirebaseDatabase.getInstance().getReference().child("PassengerDetails");

        // Initialize views
        passengerDetailsLayout = findViewById(R.id.passengerDetailsLayout);
        confirmRideButton = findViewById(R.id.confirmRideButton);

        // Retrieve data from intent
        Model model = (Model) getIntent().getSerializableExtra("model");

        // Populate views with ride details
        if (model != null) {
            TextView riderNameTextView = findViewById(R.id.textViewRiderName);
            TextView sourceAddressTextView = findViewById(R.id.textViewSourceAddress);
            TextView destinationAddressTextView = findViewById(R.id.textViewDestinationAddress);
            TextView ridePriceTextView = findViewById(R.id.textViewRidePrice);
            TextView rideDateTextView = findViewById(R.id.textViewRideDate);
            TextView rideTimeTextView = findViewById(R.id.textViewRideTime);
            TextView vehicleNumberTextView = findViewById(R.id.textViewVehicleNumber);
            TextView vehicleModelTextView = findViewById(R.id.textViewVehicleModel);
            TextView vehicleColorTextView = findViewById(R.id.textViewVehicleColor);
            TextView totalPassengersTextView = findViewById(R.id.textViewTotalPassengers);

            riderNameTextView.setText("Rider Name: " + model.getRiderName());
            sourceAddressTextView.setText("Source Address: " + model.getSourceAddress());
            destinationAddressTextView.setText("Destination Address: " + model.getDestinationAddress());
            ridePriceTextView.setText("Ride Price: " + model.getRidePrice());
            rideDateTextView.setText("Ride Date: " + model.getRideDate());
            rideTimeTextView.setText("Ride Time: " + model.getRideTime());
            vehicleNumberTextView.setText("Vehicle Number: " + model.getVehicleNumber());
            vehicleModelTextView.setText("Vehicle Model: " + model.getVehicleModel());
            vehicleColorTextView.setText("Vehicle Color: " + model.getVehicleColor());
            totalPassengersTextView.setText("Total Passengers: " + model.getTotalPassengers());

            // Load passenger details
            loadPassengerDetails(model.getUserId());

            // Button to navigate to PassengerDetailsActivity
            confirmRideButton.setOnClickListener(v -> {
                // Create an intent to start PassengerDetailsActivity
                Intent intent = new Intent(SingleRideDetailActivity.this, PassengerDetailsActivity.class);
                // Put the model as an extra in the intent
                intent.putExtra("model", model);
                // Start PassengerDetailsActivity
                startActivity(intent);
            });
        }
    }

    private void loadPassengerDetails(String userId) {
        // Remove any existing views
        passengerDetailsLayout.removeAllViews();

        // Retrieve passenger details from the database
        DatabaseReference userPassengersRef = FirebaseDatabase.getInstance().getReference().child("Rides").child(userId).child("passengers");
        userPassengersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int passengerCount = 1; // Variable to keep track of passenger number
                for (DataSnapshot passengerSnapshot : snapshot.getChildren()) {
                    // Extract passenger details
                    String name = passengerSnapshot.child("name").getValue(String.class);
                    String gender = passengerSnapshot.child("gender").getValue(String.class);

                    // Create a LinearLayout for each passenger
                    LinearLayout passengerLayout = new LinearLayout(SingleRideDetailActivity.this);
                    passengerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    passengerLayout.setOrientation(LinearLayout.VERTICAL);

                    // Set background for passenger layout
                    passengerLayout.setBackgroundResource(R.drawable.ride);

                    // Add padding to passenger layout
                    int padding = (int) getResources().getDimension(R.dimen.passenger_padding);
                    passengerLayout.setPadding(padding, padding, padding, padding);

                    // Create TextView for passenger number
                    TextView numberTextView = new TextView(SingleRideDetailActivity.this);
                    numberTextView.setText("Passenger " + passengerCount);
                    numberTextView.setTextSize(22);
                    numberTextView.setTextColor(getResources().getColor(android.R.color.black));
                    numberTextView.setTypeface(ResourcesCompat.getFont(SingleRideDetailActivity.this, R.font.main)); // Set font
                    passengerLayout.addView(numberTextView);

                    // Create TextView for passenger name
                    TextView nameTextView = new TextView(SingleRideDetailActivity.this);
                    nameTextView.setText("Name : " + name);
                    nameTextView.setTextSize(22);
                    nameTextView.setTextColor(getResources().getColor(android.R.color.black));
                    nameTextView.setTypeface(ResourcesCompat.getFont(SingleRideDetailActivity.this, R.font.main)); // Set font
                    passengerLayout.addView(nameTextView);

                    // Create TextView for passenger gender
                    TextView genderTextView = new TextView(SingleRideDetailActivity.this);
                    genderTextView.setText("Gender : " + gender);
                    genderTextView.setTextSize(22);
                    genderTextView.setTextColor(getResources().getColor(android.R.color.black));
                    genderTextView.setTypeface(ResourcesCompat.getFont(SingleRideDetailActivity.this, R.font.main)); // Set font
                    passengerLayout.addView(genderTextView);

                    // Add spacing between passenger layouts
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 0, 0, padding);
                    passengerLayout.setLayoutParams(layoutParams);

                    // Increment passenger count for next passenger
                    passengerCount++;

                    // Add passenger layout to the parent layout
                    passengerDetailsLayout.addView(passengerLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(SingleRideDetailActivity.this, "Failed to load passenger details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
