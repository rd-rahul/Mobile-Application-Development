package com.example.flytix;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private final Context context;
    private final List<Flight> flightList;

    public FlightAdapter(Context context, List<Flight> flightList) {
        this.context = context;
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.fromTxt.setText(flight.getFrom());
        holder.toTxt.setText(flight.getTo());
        holder.toShort.setText(flight.getToShort());
        holder.fromShort.setText(flight.getFromShort());
        holder.arriveTime.setText(flight.getArriveTime());

        holder.priceTxt.setText("Price: $" + flight.getPrice());
        holder.classSeatTxt.setText("Class: " + flight.getClassSeat());

        // Load airline logo
        Picasso.get().load(flight.getAirlineLogo()).into(holder.airlineLogo);

        // Navigate to SeatListActivity with flight details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SeatListActivity.class);
            intent.putExtra("flight", flight); // Pass flight object
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView fromTxt, toTxt, priceTxt, classSeatTxt, arriveTime, toShort, fromShort;
        ImageView airlineLogo;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            fromTxt = itemView.findViewById(R.id.fromTxt);
            toTxt = itemView.findViewById(R.id.toTxt);
            toShort = itemView.findViewById(R.id.txtFlightAmmount);
            fromShort = itemView.findViewById(R.id.txtDepTime);
            arriveTime = itemView.findViewById(R.id.arriveTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            classSeatTxt = itemView.findViewById(R.id.txtFlightDatePl);
            airlineLogo = itemView.findViewById(R.id.airlineLogo);
        }
    }
}
