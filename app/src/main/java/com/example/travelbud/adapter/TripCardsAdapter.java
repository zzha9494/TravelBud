package com.example.travelbud.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbud.ui.my_trips.AddTravelerActivity;
import com.example.travelbud.Trip;
import com.example.travelbud.R;
import com.example.travelbud.ui.my_trips.BudgetActivity;
import com.example.travelbud.ui.my_trips.ChatTravelerActivity;
import com.example.travelbud.ui.my_trips.ChecklistActivity;
import com.example.travelbud.ui.my_trips.DestinationsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.example.travelbud.ui.my_trips.GroupChatActivity;

import java.util.List;

public class TripCardsAdapter extends RecyclerView.Adapter<TripCardsAdapter.TripViewHolder> {

    List<Trip> trips;

    public TripCardsAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    public TripCardsAdapter() {

    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trip_card,
                parent, false);
        TripViewHolder pvh = new TripViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TripViewHolder tripViewHolder, int pos) {
        int position = tripViewHolder.getAdapterPosition();
        Trip selected_trip = trips.get(position);
        tripViewHolder.trip_name.setText(" " + selected_trip.getName());

        String travelers_name = "";


        for (int i = 0; i < selected_trip.getTravelers().size(); i++) {
            travelers_name += (i == 0 ? "" : ", ")
                    + firstLetter(selected_trip.getTravelers().get(i).getUsername());
            ;
        }

        try {
            travelers_name = travelers_name.substring(0, 30);
            travelers_name += "...";
        } catch (IndexOutOfBoundsException e) {

        }


        tripViewHolder.trip_members.setText(travelers_name);


        Button destinations = tripViewHolder.view.findViewById(R.id.show_destinations);
        Button checklist = tripViewHolder.view.findViewById(R.id.show_checklist);

        ImageButton add_travelers = tripViewHolder.view.findViewById(R.id.add_travelers);

        if (!selected_trip.getHost().equals(FirebaseAuth.getInstance().getUid())) {
            add_travelers.setVisibility(View.GONE);
        }

        destinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DestinationsActivity.class);
                intent.putExtra("selected_trip", String.valueOf(position));

                view.getContext().startActivity(intent);
                Log.i("W4K", "Click -" + position);

            }
        });
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChecklistActivity.class);
                intent.putExtra("tripKey", selected_trip.getKey());

                view.getContext().startActivity(intent);
            }
        });
        tripViewHolder.expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BudgetActivity.class);
                intent.putExtra("trip_key", selected_trip.getKey());
                view.getContext().startActivity(intent);
            }
        });

        add_travelers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTravelerActivity.class);
                intent.putExtra("selected_trip", String.valueOf(position));
                view.getContext().startActivity(intent);
            }
        });

        tripViewHolder.chat_travelers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatTravelerActivity.class);
                intent.putExtra("trip_key", selected_trip.getKey());
                intent.putExtra("trip_name", selected_trip.getName());
                v.getContext().startActivity(intent);
            }
        });

//        tripViewHolder.cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), GroupChatActivity.class);
//                intent.putExtra("is_group_chat", true);
//                intent.putExtra("selected_trip", String.valueOf(position));
//                v.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView trip_name;
        TextView trip_members;
        ImageButton add_travelers, chat_travelers;
        View view;               // <----- here
        Button destinations;
        Button expense;


        public TripViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.trip_card);
            trip_name = (TextView) itemView.findViewById(R.id.trip_name);
            trip_members = (TextView) itemView.findViewById(R.id.trip_members);
            add_travelers = (ImageButton) itemView.findViewById(R.id.add_travelers);
            chat_travelers = (ImageButton) itemView.findViewById(R.id.group_chat);
            expense = (Button) itemView.findViewById(R.id.show_expense);
            this.view = itemView;            // <----- here

        }
    }

    public static String firstLetter(String name) {
        String firstLetStr = name.substring(0, 1);
        String remLetStr = name.substring(1);
        firstLetStr = firstLetStr.toUpperCase();
        return firstLetStr + remLetStr;
    }

}

