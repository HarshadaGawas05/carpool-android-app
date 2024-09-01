package com.project.carpool.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.carpool.R;

import java.util.Calendar;
import java.util.HashMap;

public class CreateRideFragment extends Fragment {

    EditText riderNameEditTxt, sourceAddressEditTxt, destinationAddressEditTxt, totalPassengersEditTxt, ridePriceEditTxt, phoneNumberEditTxt,
            vehicleNumberEditTxt, vehicleModelEditTxt, vehicleColorEditTxt, rideDateTxt, rideTimeTxt;
    Button createRideBtn;
    Calendar calendar;
    int currentHour, currentMinute;
    String amPm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_ride, container, false);

        riderNameEditTxt = view.findViewById(R.id.RiderNameEdit);
        sourceAddressEditTxt = view.findViewById(R.id.SourceAddressEdit);
        destinationAddressEditTxt = view.findViewById(R.id.DestinationAddressEdit);
        totalPassengersEditTxt = view.findViewById(R.id.TotalPassengersEdit);
        ridePriceEditTxt = view.findViewById(R.id.RidePriceEdit);
        phoneNumberEditTxt = view.findViewById(R.id.PhoneNumberEdit);
        vehicleNumberEditTxt = view.findViewById(R.id.VehicleNumberEdit);
        vehicleModelEditTxt = view.findViewById(R.id.VehicleModelEdit);
        vehicleColorEditTxt = view.findViewById(R.id.VehicleColorEdit);
        rideDateTxt = view.findViewById(R.id.RideDateEdit);
        rideTimeTxt = view.findViewById(R.id.RideTimeEdit);
        createRideBtn = view.findViewById(R.id.CreateRideBtn);

        rideDateTxt.setOnClickListener(v -> showDatePickerDialog());
        rideTimeTxt.setOnClickListener(v -> showTimePickerDialog());

        createRideBtn.setOnClickListener(v -> createRide());

        return view;
    }

    private void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth1) ->
                        rideDateTxt.setText(dayOfMonth1 + "/" + (month1 + 1) + "/" + year1),
                year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    if (hourOfDay >= 12) {
                        amPm = "PM";
                    } else {
                        amPm = "AM";
                    }
                    rideTimeTxt.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                }, currentHour, currentMinute, false);
        timePickerDialog.show();
    }

    private void createRide() {
        String riderName = riderNameEditTxt.getText().toString().trim();
        String sourceAddress = sourceAddressEditTxt.getText().toString().trim();
        String destinationAddress = destinationAddressEditTxt.getText().toString().trim();
        String totalPassengers = totalPassengersEditTxt.getText().toString().trim();
        String rideDate = rideDateTxt.getText().toString().trim();
        String rideTime = rideTimeTxt.getText().toString().trim();
        String ridePrice = ridePriceEditTxt.getText().toString().trim();
        String phoneNumber = phoneNumberEditTxt.getText().toString().trim();
        String vehicleNumber = vehicleNumberEditTxt.getText().toString().trim();
        String vehicleModel = vehicleModelEditTxt.getText().toString().trim();
        String vehicleColor = vehicleColorEditTxt.getText().toString().trim();

        if (riderName.isEmpty() || sourceAddress.isEmpty() || destinationAddress.isEmpty() || totalPassengers.isEmpty() ||
                rideDate.isEmpty() || rideTime.isEmpty() || ridePrice.isEmpty() || phoneNumber.isEmpty() || vehicleNumber.isEmpty() ||
                vehicleModel.isEmpty() || vehicleColor.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all the details", Toast.LENGTH_SHORT).show();
        } else {
            addDataToFirebase(riderName, sourceAddress, destinationAddress, totalPassengers, rideDate, rideTime, ridePrice, phoneNumber,
                    vehicleNumber, vehicleModel, vehicleColor);
        }
    }

    private void addDataToFirebase(String riderName, String sourceAddress, String destinationAddress, String totalPassengers, String rideDate,
                                   String rideTime, String ridePrice, String phoneNumber, String vehicleNumber, String vehicleModel, String vehicleColor) {

        String userId = GoogleSignIn.getLastSignedInAccount(getActivity()).getId();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Rides");

        String sourceAddrAndDestinationAddr = sourceAddress + destinationAddress;

        HashMap<String, String> user_details = new HashMap<>();

        user_details.put("riderName", riderName);
        user_details.put("sourceAddress", sourceAddress);
        user_details.put("destinationAddress", destinationAddress);
        user_details.put("sourceAddrAndDestinationAddr", sourceAddrAndDestinationAddr);
        user_details.put("rideDate", rideDate);
        user_details.put("rideTime", rideTime);
        user_details.put("ridePrice", ridePrice);
        user_details.put("totalPassengers", totalPassengers);
        user_details.put("userId", userId);
        user_details.put("phoneNumber", phoneNumber);
        user_details.put("vehicleNumber", vehicleNumber);
        user_details.put("vehicleModel", vehicleModel);
        user_details.put("vehicleColor", vehicleColor);

        myRef.child(userId).setValue(user_details).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Ride created successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(getContext(), "Failed to create ride", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        riderNameEditTxt.setText("");
        sourceAddressEditTxt.setText("");
        destinationAddressEditTxt.setText("");
        totalPassengersEditTxt.setText("");
        rideDateTxt.setText("");
        rideTimeTxt.setText("");
        ridePriceEditTxt.setText("");
        phoneNumberEditTxt.setText("");
        vehicleNumberEditTxt.setText("");
        vehicleModelEditTxt.setText("");
        vehicleColorEditTxt.setText("");
    }
}
