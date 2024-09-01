package com.project.carpool;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.carpool.Activities.StartingActivity;
import com.project.carpool.Fragments.CreateRideFragment;
import com.project.carpool.Fragments.HomeFragment;
import com.project.carpool.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mbottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Assigning the bottom navigation to navigate between the fragments
        mbottomNavigationView = findViewById(R.id.BottomNavigationView);
        Menu menuNav = mbottomNavigationView.getMenu();

        // Setting the homeFragment as default Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new HomeFragment()).commit();

        // Set initial icon and text tint colors
        setIconAndTextTintColors();

        // BottomNavigationMethod to perform bottomNavigation Action
        mbottomNavigationView.setOnNavigationItemSelectedListener(bottomnavMethod);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Checks if the user is logged in or not
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            // Redirect to the starting Activity if the user is not logged in
            Intent intent = new Intent(MainActivity.this, StartingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Performs the Bottom Navigation action
    private BottomNavigationView.OnNavigationItemSelectedListener bottomnavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    // Setting the fragment to null
                    Fragment fragment = null;

                    // Navigates between the activities by using the id of the menu
                    if (item.getItemId() == R.id.homeMenu) {
                        fragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.createRideMenu) {
                        fragment = new CreateRideFragment();
                    } else if (item.getItemId() == R.id.profileMenu) {
                        fragment = new ProfileFragment();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment).commit();
                    return true;
                }
            };

    // Method to set icon and text tint colors
    private void setIconAndTextTintColors() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked}
        };

        int[] selectedColors = new int[]{
                getResources().getColor(R.color.icon_selected_color),
                getResources().getColor(R.color.icon_unselected_color)
        };

        int[] unselectedColors = new int[]{
                getResources().getColor(R.color.icon_unselected_color),
                getResources().getColor(R.color.icon_unselected_color)
        };

        ColorStateList selectedColorStateList = new ColorStateList(states, selectedColors);
        ColorStateList unselectedColorStateList = new ColorStateList(states, unselectedColors);

        mbottomNavigationView.setItemIconTintList(selectedColorStateList);
        mbottomNavigationView.setItemTextColor(selectedColorStateList);
    }
}