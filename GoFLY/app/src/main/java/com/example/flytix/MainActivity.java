package com.example.flytix;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Spinner spFrom, spTo, spFlightClass;
    ProgressBar fromPB, toPB;
    TextView adultTxt, childTxt, adultDe, adultIn, childDe, childIn, flightDateTxt, txtUsername; // Added txtUsername
    Button btnSearchFlight;

    private DatabaseReference locationDbRef, flightDbRef;
    private String selectedDate;
    private int adultCount = 1;
    private int childCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spFrom = findViewById(R.id.fromSp);
        spTo = findViewById(R.id.toSp);
        spFlightClass = findViewById(R.id.spinnerFlightClass);
        fromPB = findViewById(R.id.progressBarFrom);
        toPB = findViewById(R.id.progressBarTo);
        adultTxt = findViewById(R.id.txtAdult);
        childTxt = findViewById(R.id.txtChild);
        adultDe = findViewById(R.id.decrementAdult);
        adultIn = findViewById(R.id.incrementAdult);
        childDe = findViewById(R.id.decrementChild);
        childIn = findViewById(R.id.incrementChild);
        flightDateTxt = findViewById(R.id.txtFlightDate);
        btnSearchFlight = findViewById(R.id.buttonSearchFlight);
        txtUsername = findViewById(R.id.txtUserName); // Initialize txtUsername

        // Retrieve username passed from LoginActivity
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            txtUsername.setText(username); // Display the username
        } else {
            txtUsername.setText("Guest"); // Fallback for missing username
        }

        // Initialize Firebase Database references
        flightDbRef = FirebaseDatabase.getInstance("https://gofly-72506-default-rtdb.firebaseio.com/").getReference().child("Flights");

        // Set default values for adult and child count
        adultTxt.setText("1 Adult");
        childTxt.setText("1 Child");

        loadLocations();
        setupFlightClassDropdown();
        flightDateTxt.setOnClickListener(view -> showDatePickerDialog());
        btnSearchFlight.setOnClickListener(view -> searchFlight());
        setupCounterButtons();
    }

    private void loadLocations() {
        List<String> locations = new ArrayList<>();
        locations.add("Los Angeles");
        locations.add("New York");
        locations.add("San Francisco");
        locations.add("Chicago");
        locations.add("Miami");
        locations.add("Seattle");
        locations.add("Boston");
        locations.add("Philadelphia");
        locations.add("Las Vegas");
        locations.add("Washington DC");

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spFrom.setAdapter(locationAdapter);
        spTo.setAdapter(locationAdapter);                // Hide progress bars after loading is complete
        fromPB.setVisibility(View.GONE);
        toPB.setVisibility(View.GONE);
            }

    private void setupFlightClassDropdown() {
        List<String> flightClasses = new ArrayList<>();
        flightClasses.add("Business");
        flightClasses.add("First");
        flightClasses.add("Economy");

        ArrayAdapter<String> flightClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, flightClasses);
        flightClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFlightClass.setAdapter(flightClassAdapter);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                    selectedDate = sdf.format(calendar.getTime());
                    flightDateTxt.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void setupCounterButtons() {
        adultIn.setOnClickListener(view -> {
            adultCount++;
            adultTxt.setText(adultCount + " Adult");
        });

        adultDe.setOnClickListener(view -> {
            if (adultCount > 1) {
                adultCount--;
                adultTxt.setText(adultCount + " Adult");
            }
        });

        childIn.setOnClickListener(view -> {
            childCount++;
            childTxt.setText(childCount + " Child");
        });

        childDe.setOnClickListener(view -> {
            if (childCount > 1) {
                childCount--;
                childTxt.setText(childCount + " Child");
            }
        });
    }

    private void searchFlight() {
        String fromLocation = spFrom.getSelectedItem().toString(); // User-selected From location
        String toLocation = spTo.getSelectedItem().toString();     // User-selected To location
//        Toast.makeText(MainActivity.this, fromLocation, Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, toLocation, Toast.LENGTH_SHORT).show();
        if (fromLocation.isEmpty() || toLocation.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please select both From and To locations.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query Firebase for flights matching LocationA (From) and LocationB (To)
        flightDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> matchingFlights = new ArrayList<>();

                for (DataSnapshot routeSnapshot : dataSnapshot.getChildren()) {
                    String locationA = routeSnapshot.child("LocationA").getValue(String.class);
                    String locationB = routeSnapshot.child("LocationB").getValue(String.class);
                    Log.d("FlightSearchRoute", "RouteSnapshot: " + routeSnapshot.toString());
                    Log.d("FlightSearch", "LocationA: " + locationA);
                    Log.d("FlightSearch", "LocationB: " + locationB);
                    if (fromLocation.equals(locationA) && toLocation.equals(locationB)) {
                        // Found a matching route; fetch all flights for this route

                        DataSnapshot flightsSnapshot = routeSnapshot.child("Flights");
                        for (DataSnapshot flightSnapshot : flightsSnapshot.getChildren()) {
                            String flightNumber = flightSnapshot.child("FlightNumber").getValue(String.class);
                            String departureTime = flightSnapshot.child("DepartureTime").getValue(String.class);
                            String arrivalTime = flightSnapshot.child("ArrivalTime").getValue(String.class);
                            String airline = flightSnapshot.child("Airline").getValue(String.class);
                            String seatClass=flightSnapshot.child("Class").getValue(String.class);
                            String price=flightSnapshot.child("Price").getValue(String.class);

                            // Format flight details as a string and add to the list
                            matchingFlights.add(departureTime + "\n"
                                    + arrivalTime + "\n"
                                    + "Arrival: " + arrivalTime + "\n"
                                     + airline + "\n"
                                    + "Class: " + seatClass + "\n"
                                    + "Price: " + price +"\n"
                                    + locationA + "\n"
                                    + locationB
                            );
                        }
                    }
                }

                if (!matchingFlights.isEmpty()) {
                    // Pass the matchingFlights list to SearchActivity
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putStringArrayListExtra("matchingFlights", matchingFlights); // Pass data
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No flights found for the selected route.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to search for flights.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

