package com.project.carpool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.carpool.Fragments.HomeFragment;
import com.project.carpool.R;

public class StartingActivity extends AppCompatActivity {
    private Button getStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        // Initialize Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, redirect to HomeActivity or MainActivity
            Intent intent = new Intent(getApplicationContext(), HomeFragment.class); // Change to appropriate activity
            startActivity(intent);
            finish(); // Finish the current activity
        }

        // Assigning the Button to perform Appropriate Action
        getStartedBtn = findViewById(R.id.GetStartedBtn);

        // Implementing the onClickListener to Change the intent
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
