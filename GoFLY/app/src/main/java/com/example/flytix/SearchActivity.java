package com.example.flytix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView searchView;
    private FlightAdapter flightAdapter;
    private List<Flight> flightList; // Holds parsed Flight objects
    ImageView backBtn;
    ProgressBar progressBarSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backBtn = findViewById(R.id.backBtn);
        progressBarSearch = findViewById(R.id.progressBarSearch);

        // Initialize RecyclerView and set layout manager
        searchView = findViewById(R.id.searchView);
        searchView.setLayoutManager(new LinearLayoutManager(this));

        flightList = new ArrayList<>();
        flightAdapter = new FlightAdapter(this, flightList);
        searchView.setAdapter(flightAdapter);

        // Back button functionality
        backBtn.setOnClickListener(v -> finish());

        // Retrieve matchingFlights list from Intent
        Intent intent = getIntent();
        ArrayList<String> matchingFlights = intent.getStringArrayListExtra("matchingFlights");

        if (matchingFlights != null && !matchingFlights.isEmpty()) {
            // Populate flightList with the received matchingFlights
            loadMatchingFlights(matchingFlights);
        } else {
            Toast.makeText(this, "No flights found.", Toast.LENGTH_SHORT).show();
        }

        // Hide the progress bar after loading data
        progressBarSearch.setVisibility(View.GONE);
    }

    private void loadMatchingFlights(ArrayList<String> matchingFlights) {
        for (String flightDetails : matchingFlights) {
            Log.d("FlightSearch", "flights "+flightDetails );
            Flight flight = parseFlightDetails(flightDetails);
            if (flight != null) {
                flightList.add(flight);
            }
        }

        flightAdapter.notifyDataSetChanged();
    }

    private Flight parseFlightDetails(String flightDetails) {
        // Parse the string into a Flight object (example parsing logic)
        String[] details = flightDetails.split("\n");
        if (details.length >= 4) {
            Flight flight = new Flight();
            flight.setFrom(details[0].replace("Departure: ", "").trim());
            flight.setTo(details[1].replace("Arrival: ", "").trim());
            flight.setTime(details[2].replace("Departure: ", "").trim());
            flight.setArriveTime(details[3].replace("Arrival: ", "").trim());
            flight.setClassSeat(details[4].replace("Class: ", "").trim());
            flight.setPrice(Double.parseDouble(details[5].replace("Price: $", "").trim()));
            flight.setFromLocation(details[6]);
            flight.setToLocation(details[7]);
            // Set a default airline logo if not provided

            return flight;
        }
        return null;
    }
}
