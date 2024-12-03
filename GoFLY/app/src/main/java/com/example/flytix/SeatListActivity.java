package com.example.flytix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SeatListActivity extends AppCompatActivity {
    private GridLayout seatLayout;
    private ArrayList<Integer> selectedSeats,sortSelectedSeats; // To store selected seat numbers
    private Flight selectedFlight;
    private TextView selectedSeatsText,fromLocation,toLocation,fromLocationTime,toLocationTime;
    private Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_list);

        confirmButton=findViewById(R.id.confirmBookingButton);
        // Find GridLayout
        seatLayout = findViewById(R.id.seatLayout);
        fromLocation=findViewById(R.id.fromLocation);
        toLocation=findViewById(R.id.toLocation);
        fromLocationTime=findViewById(R.id.fromLocationTime);
        toLocationTime=findViewById(R.id.toLocationTime);
        selectedSeatsText = findViewById(R.id.selectedSeatsText); // Find the TextView for selected seats
        selectedSeats = new ArrayList<>();

        // Retrieve the selected Flight object
        selectedFlight = (Flight) getIntent().getSerializableExtra("flight");
        String fromLocationA = selectedFlight.getFromLocation();
        String toLocationA = selectedFlight.getToLocation();
        String fromLocationTimeA=selectedFlight.getFrom();
        String toLocationTimeA=selectedFlight.getTo();
        fromLocation.setText(fromLocationA);
        toLocation.setText(toLocationA);
        fromLocationTime.setText(fromLocationTimeA);
        toLocationTime.setText(toLocationTimeA);

        // Add seats dynamically
        setupSeats();
        confirmButton.setOnClickListener(v->{
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("flight", selectedFlight);
            intent.putExtra("selectedSeats", selectedSeats); // Pass ArrayList directly, handle in PaymentActivity
            startActivity(intent) ;       });
    }


    private void setupSeats() {
        // Total seats (use from the Flight object, default to 20 if not set)
        int totalSeats = selectedFlight.getNumberSeat() == 0 ? 20 : selectedFlight.getNumberSeat();

        for (int i = 1; i <= totalSeats; i++) {
            Button seatButton = new Button(this);
            seatButton.setText(String.valueOf(i)); // Set seat number as text
            seatButton.setBackgroundResource(R.drawable.seat_available); // Set default background

            // Set button size and margins
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 200; // Width in pixels
            params.height = 200; // Height in pixels
            params.setMargins(10, 10, 10, 10); // Margins around the button
            seatButton.setLayoutParams(params);

            // Set click listener for seat selection
            seatButton.setOnClickListener(v -> {
                int seatNumber = Integer.parseInt(seatButton.getText().toString());
                if (selectedSeats.contains(seatNumber)) {
                    // Deselect seat
                    selectedSeats.remove((Integer) seatNumber);
                    seatButton.setBackgroundResource(R.drawable.seat_available);
                } else {
                    // Select seat
                    selectedSeats.add(seatNumber);
                    seatButton.setBackgroundResource(R.drawable.seat_selected);
                }
                updateSelectedSeatsText();
            });

            // Add button to the GridLayout
            seatLayout.addView(seatButton);
        }
    }
    private ArrayList<Integer> sortOf(ArrayList<Integer> selectedSeats) {
        ArrayList<Integer> sortedSeats = new ArrayList<>(selectedSeats);
        Collections.sort(sortedSeats);
        return sortedSeats; // Return the sorted list
    }
    private void updateSelectedSeatsText() {
        if (selectedSeats.isEmpty()) {
            selectedSeatsText.setText("Selected Seats: None");
        } else {
            sortSelectedSeats=sortOf(selectedSeats);
            selectedSeatsText.setText("Selected Seats: " + sortSelectedSeats.toString());
        }
    }
}
