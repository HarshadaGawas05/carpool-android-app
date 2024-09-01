package com.project.carpool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.project.carpool.Activities.SingleRideDetailActivity;
import com.project.carpool.Model.Model;
import com.project.carpool.R;

public class Adapter extends FirebaseRecyclerAdapter<Model, Adapter.ViewHolder> {

    private Context mContext;

    public Adapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_ride_details, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView riderNameTextView;
        private TextView sourceAddressTextView;
        private TextView destinationAddressTextView;
        private TextView rideDateTextView;
        private TextView rideTimeTextView;
        private TextView ridePriceTextView;
        private TextView vehicleNumberTextView;
        private TextView vehicleModelTextView;
        private TextView vehicleColorTextView;
        private TextView totalPassengersTextView;
        private Button bookRideButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            riderNameTextView = itemView.findViewById(R.id.textViewRiderName);
            sourceAddressTextView = itemView.findViewById(R.id.textViewSourceAddress);
            destinationAddressTextView = itemView.findViewById(R.id.textViewDestinationAddress);
            rideDateTextView = itemView.findViewById(R.id.textViewRideDate);
            rideTimeTextView = itemView.findViewById(R.id.textViewRideTime);
            ridePriceTextView = itemView.findViewById(R.id.textViewRidePrice);
            vehicleNumberTextView = itemView.findViewById(R.id.textViewVehicleNumber);
            vehicleModelTextView = itemView.findViewById(R.id.textViewVehicleModel);
            vehicleColorTextView = itemView.findViewById(R.id.textViewVehicleColor);
            totalPassengersTextView = itemView.findViewById(R.id.textViewTotalPassengers);
            bookRideButton = itemView.findViewById(R.id.BookRideBtn);
        }

        void bind(Model model) {
            riderNameTextView.setText("Rider Name: " + model.getRiderName());
            sourceAddressTextView.setText("Source Address: " + model.getSourceAddress());
            destinationAddressTextView.setText("Destination Address: " + model.getDestinationAddress());
            rideDateTextView.setText("Ride Date: " + model.getRideDate());
            rideTimeTextView.setText("Ride Time: " + model.getRideTime());
            ridePriceTextView.setText("Ride Price: " + model.getRidePrice());
            vehicleNumberTextView.setText("Vehicle Number: " + model.getVehicleNumber());
            vehicleModelTextView.setText("Vehicle Model: " + model.getVehicleModel());
            vehicleColorTextView.setText("Vehicle Color: " + model.getVehicleColor());
            totalPassengersTextView.setText("Total Passengers: " + model.getTotalPassengers());

            bookRideButton.setOnClickListener(v -> {
                Context context = v.getContext();

                // Open SingleRideDetailActivity
                Intent intent = new Intent(context, SingleRideDetailActivity.class);
                intent.putExtra("model", model);
                context.startActivity(intent);
            });

        }
    }
}
