package com.project.carpool.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.carpool.Adapter.Adapter;
import com.project.carpool.Model.Model;
import com.project.carpool.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    Button searchRideButton;
    EditText sourceAddressEditText, destinationAddressEditText;
    Adapter adapter;
    RecyclerView recyclerView;
    ProgressBar loadingProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchRideButton = view.findViewById(R.id.SearchRideBtn);
        sourceAddressEditText = view.findViewById(R.id.SearchSourceEdit);
        destinationAddressEditText = view.findViewById(R.id.SearchDestinationEdit);
        recyclerView = view.findViewById(R.id.RecyclerView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchRideButton.setOnClickListener(v -> searchRide());

        loadAllRides();

        return view;
    }

    private void searchRide() {
        // Show loading indicator
        loadingProgressBar.setVisibility(View.VISIBLE);

        String sourceAddress = sourceAddressEditText.getText().toString().trim().toLowerCase();
        String destinationAddress = destinationAddressEditText.getText().toString().trim().toLowerCase();
        String sourceAndDestinationAddr = sourceAddress + destinationAddress;

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rides")
                                .orderByChild("sourceAddrAndDestinationAddr").equalTo(sourceAndDestinationAddr), Model.class)
                        .build();

        adapter = new Adapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        // Hide loading indicator after data is loaded
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void loadAllRides() {
        // Show loading indicator
        loadingProgressBar.setVisibility(View.VISIBLE);

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rides"), Model.class)
                        .build();

        adapter = new Adapter(options);
        recyclerView.setAdapter(adapter);

        // Hide loading indicator after data is loaded
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
