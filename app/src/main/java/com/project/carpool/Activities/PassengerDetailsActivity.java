package com.project.carpool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.carpool.Model.Model;
import com.project.carpool.R;

public class PassengerDetailsActivity extends AppCompatActivity {

    private EditText passengerCountEditText;
    private TextView totalPassengersTextView;
    private TextView upiIdTextView;
    private Button confirmButton;
    private DatabaseReference ridesRef;
    private Model model;
    private int currentTotalPassengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);

        // Initialize Firebase database reference
        ridesRef = FirebaseDatabase.getInstance().getReference().child("Rides");

        // Initialize views
        passengerCountEditText = findViewById(R.id.passengerCountEditText);
        totalPassengersTextView = findViewById(R.id.totalPassengersTextView);
        confirmButton = findViewById(R.id.confirmButton);

        // Retrieve the total number of passengers and UPI ID from the intent
        Intent intent = getIntent();
        if (intent != null) {
            model = (Model) intent.getSerializableExtra("model");
            if (model != null) {
                currentTotalPassengers = Integer.parseInt(model.getTotalPassengers());
                totalPassengersTextView.setText("Total Passengers: " + currentTotalPassengers);
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDetails();
            }
        });
    }

    public void addPassengers(View view) {
        // Get the entered number of passengers
        String countStr = passengerCountEditText.getText().toString();
        if (countStr.isEmpty()) {
            // Show a message or handle the case when the EditText is empty
            return;
        }

        int count = Integer.parseInt(countStr);

        // Check if the entered number of passengers exceeds the total available
        if (count > currentTotalPassengers) {
            Toast.makeText(this, "Cannot add more passengers than available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear existing views
        LinearLayout layout = findViewById(R.id.passengerDetail);
        layout.removeAllViews();

        // Add passenger EditText fields dynamically
        for (int i = 0; i < count; i++) {
            View passengerView = getLayoutInflater().inflate(R.layout.passenger_edittext_item, null);
            layout.addView(passengerView);
        }
    }

    private void confirmDetails() {
        // Get the entered number of passengers
        String countStr = passengerCountEditText.getText().toString();
        if (countStr.isEmpty()) {
            Toast.makeText(this, "Please enter the number of passengers", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = Integer.parseInt(countStr);

        // Check if the entered number of passengers exceeds the total available
        if (count > currentTotalPassengers) {
            Toast.makeText(this, "Cannot confirm with more passengers than available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if all passenger details are entered
        LinearLayout layout = findViewById(R.id.passengerDetail);
        if (layout.getChildCount() < count) {
            Toast.makeText(this, "Please enter details for all passengers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Deduct the entered number of passengers from the total available
        int newTotalPassengers = currentTotalPassengers - count;
        totalPassengersTextView.setText("Total Passengers: " + newTotalPassengers);
        model.setTotalPassengers(String.valueOf(newTotalPassengers));

        // Get the userID from the Model
        String userID = model.getUserId();

        // Update the total available passengers under the user's node in the database
        ridesRef.child(userID).child("totalPassengers").setValue(String.valueOf(newTotalPassengers));

        // Store passenger details in the database under a unique node for each user
        for (int i = 0; i < count; i++) {
            // Get the passenger EditText fields
            View passengerView = layout.getChildAt(i);
            EditText nameEditText = passengerView.findViewById(R.id.passengerNameEditText);
            RadioGroup genderRadioGroup = passengerView.findViewById(R.id.genderRadioGroup);

            // Get passenger details
            String passengerName = nameEditText.getText().toString().trim();
            // Get selected radio button ID
            int selectedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = passengerView.findViewById(selectedRadioButtonId);
            // Check if any radio button is selected
            String passengerGender = (selectedRadioButton != null) ? selectedRadioButton.getText().toString() : "";

            // Generate a unique key for each passenger
            String passengerKey = ridesRef.child(userID).child("passengers").push().getKey();

            // Create a new node under the user's node to store passenger details
            DatabaseReference passengerRef = ridesRef.child(userID).child("passengers").child(passengerKey);

            // Store passenger details in the database
            passengerRef.child("name").setValue(passengerName);
            passengerRef.child("gender").setValue(passengerGender);
        }
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("sourceAddress", model.getSourceAddress());
        intent.putExtra("destinationAddress", model.getDestinationAddress());
        startActivity(intent);
        finish();
    }
}
