package com.project.carpool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.carpool.MainActivity;
import com.project.carpool.R;

public class EditDetailsActivity extends AppCompatActivity {

    EditText updateEmail, updatePhoneNumber, updateName;
    Button submitDataBtn;
    RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        updateEmail = findViewById(R.id.updateEmailEditTxt);
        updatePhoneNumber = findViewById(R.id.updatePhoneNumberEditTxt);
        updateName = findViewById(R.id.updateNameEditTxt);
        submitDataBtn = findViewById(R.id.submitDetailsBtn);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        // Get currently signed-in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Get the user's unique ID
            String userId = user.getUid();

            // Access the Firebase database reference for this user
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            // Retrieve the user's details from the database
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get the user's details
                        String email = snapshot.child("email").getValue(String.class);
                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        String name = snapshot.child("name").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);

                        // Update the EditText fields with the retrieved details
                        updateEmail.setText(email);
                        updatePhoneNumber.setText(phoneNumber);
                        updateName.setText(name);

                        // Check the gender RadioButton based on the retrieved gender
                        if (gender != null) {
                            if (gender.equals("Male")) {
                                ((RadioButton) findViewById(R.id.radioMale)).setChecked(true);
                            } else if (gender.equals("Female")) {
                                ((RadioButton) findViewById(R.id.radioFemale)).setChecked(true);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Toast.makeText(EditDetailsActivity.this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
                }
            });
        }

        submitDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get updated email, phone number, and name from EditText fields
                String updatedEmail = updateEmail.getText().toString().trim();
                String updatedPhoneNumber = updatePhoneNumber.getText().toString().trim();
                String updatedName = updateName.getText().toString().trim();

                // Get selected gender from RadioButton
                String selectedGender = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();

                // Get currently signed-in user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Get the user's unique ID
                    String userId = user.getUid();

                    // Access the Firebase database reference for this user
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                    // Update user details in the database
                    userRef.child("email").setValue(updatedEmail);
                    userRef.child("phoneNumber").setValue(updatedPhoneNumber);
                    userRef.child("name").setValue(updatedName);
                    userRef.child("gender").setValue(selectedGender);

                    // Display a success message
                    Toast.makeText(EditDetailsActivity.this, "User details updated successfully", Toast.LENGTH_SHORT).show();

                    // Navigate back to the main activity
                    startActivity(new Intent(EditDetailsActivity.this, MainActivity.class));
                    finish(); // Optional: Finish this activity to prevent the user from navigating back to it using the back button
                }
            }
        });
    }
}
