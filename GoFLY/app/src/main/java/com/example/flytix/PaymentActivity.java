package com.example.flytix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    TextView fromLocation,toLocation,departureTime,arrivalTime,classFlight,amountFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize views
        fromLocation=findViewById(R.id.fromLocation);
        toLocation=findViewById(R.id.toLocation);
        departureTime=findViewById(R.id.departureTime);
        arrivalTime=findViewById(R.id.arrivalTime);
        classFlight=findViewById(R.id.classFlight);
        amountFlight=findViewById(R.id.amountFlight);

        // Retrieve the data passed from the previous activity
        Flight flights = (Flight) getIntent().getSerializableExtra("flight");

        String LocationA=flights.getFromLocation();
        String LocationB=flights.getToLocation();
        String departureTimes=flights.getFrom();
        String arrivalTimes=flights.getTo();
        String classes= flights.getClassSeat();
        Double price= flights.getPrice();


        ArrayList selectedSeats=(ArrayList<Integer>)getIntent().getSerializableExtra("selectedSeats");
        fromLocation.setText(LocationA);
        toLocation.setText(LocationB);
        departureTime.setText(departureTimes);
        arrivalTime.setText(arrivalTimes);
        Double newPrice=selectedSeats.size()*price;
        classFlight.setText(classes);
        amountFlight.setText("$"+newPrice.toString());

//

//
//
//
//        double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);
//        int numSelectedSeats = intent.getIntExtra("numSelectedSeats", 1); // default to 1 seat
//        String firstName = intent.getStringExtra("firstName");
//        String idNumber = intent.getStringExtra("idNumber");
//        String mobileNumber = intent.getStringExtra("mobileNumber");
//        String nameSelectedSeats = intent.getStringExtra("selectedSeats");
//        String gender = intent.getStringExtra("gender");
//
//        // Display flight details
//        if (flight != null) {
//            txtFrom.setText(flight.getFrom());
//            txtTo.setText(flight.getTo());
//            txtTravelDate.setText(flight.getDate());
//            txtTravelTime.setText(flight.getTime());
//        }
//
//        // Set the number of seats and total price
//        txtNumberOfSeats.setText(String.valueOf(numSelectedSeats));
//        txtFlightAmount.setText("$" + totalPrice);
//
//
//        // Pay button functionality
//        paybtn.setOnClickListener(v -> {
//            // Intent in PassengerDetailsActivity
//            Intent ticket = new Intent(PaymentActivity.this, GPayActivity.class);
//            // Passing flight object
//            ticket.putExtra("flight", flight);
//            ticket.putExtra("firstName", firstName);
//            ticket.putExtra("idNumber", idNumber);
//            ticket.putExtra("mobileNumber", mobileNumber);
//            ticket.putExtra("gender", gender);
//            ticket.putExtra("totalPrice", totalPrice); // Total price of selected seats
//            ticket.putExtra("nameSelectedSeats", nameSelectedSeats); // Name of seats selected
//
//            // Start PaymentActivity
//            startActivity(ticket);
//
//        });
//
//        // Back button functionality
//        backBtn.setOnClickListener(v -> onBackPressed());
    }
}
