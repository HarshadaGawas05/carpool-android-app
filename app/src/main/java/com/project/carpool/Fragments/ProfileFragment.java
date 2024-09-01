package com.project.carpool.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.carpool.Activities.EditDetailsActivity;
import com.project.carpool.Activities.LoginActivity;
import com.project.carpool.R;

public class ProfileFragment extends Fragment {

    private TextView textViewUserName, textViewUserEmail, textViewUserPhone, textViewUserGender;
    private Button buttonEditProfile, buttonLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUserName = view.findViewById(R.id.textViewUserName);
        textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
        textViewUserPhone = view.findViewById(R.id.textViewUserPhone);
        textViewUserGender = view.findViewById(R.id.textViewUserGender);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        // Set click listener for the edit profile button
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open edit profile activity
                startActivity(new Intent(getActivity(), EditDetailsActivity.class));
            }
        });

        // Set click listener for the logout button
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                // Redirect to login activity or perform necessary action
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                // Finish the current activity
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the user details when the fragment resumes
        refreshUserDetails();
    }

    private void refreshUserDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.child("name").getValue(String.class);
                        String userEmail = snapshot.child("email").getValue(String.class);
                        String userPhone = snapshot.child("phoneNumber").getValue(String.class);
                        String userGender = snapshot.child("gender").getValue(String.class);

                        textViewUserName.setText("User Name : " + (userName != null ? userName : "N/A"));
                        textViewUserEmail.setText("Email : " + (userEmail != null ? userEmail : "N/A"));
                        textViewUserPhone.setText("Phone : " + (userPhone != null ? userPhone : "N/A"));
                        textViewUserGender.setText("Gender : " + (userGender != null ? userGender : "N/A"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
