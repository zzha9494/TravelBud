package com.example.travelbud.ui.my_trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.travelbud.Destination;
import com.example.travelbud.R;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.adapter.DestinationCardsAdapter;
import com.example.travelbud.databinding.ActivityDestinationsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DestinationsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityDestinationsBinding binding;
    private TravelBudUser current_user;
    private Fragment destinations_map;
    private int trip_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        trip_index = Integer.parseInt(bundle.getString("selected_trip"));

        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

        SharedPreferences prefs = this.getSharedPreferences("user_token", Context.MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);
        myTripsViewModel.getUser(user_token).observe(this, user -> {
            current_user = user;
            RecyclerView rv = (RecyclerView) findViewById(R.id.destinations_rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            rv.setLayoutManager(llm);
            try {
                DestinationCardsAdapter adapter =
                        new DestinationCardsAdapter(user.getTrips().get(trip_index).getDestinations());
                rv.setAdapter(adapter);
            }catch (Exception e){
                List<Destination> empty = new ArrayList<>();
                DestinationCardsAdapter adapter =user.getTrips().size() == 0? new DestinationCardsAdapter(empty):
                        new DestinationCardsAdapter(user.getTrips().get(trip_index).getDestinations());
                rv.setAdapter(adapter);
            }
        });
        getSupportActionBar().setTitle("Destinations");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.destinations_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

//        List<Destination> destinations = current_user.getTrips().get(trip_index)
//        .getDestinations();
//        destinations.forEach(destination -> {
//
//        });

        SharedPreferences prefs = this.getSharedPreferences("user_token", Context.MODE_PRIVATE);
        String user_token = prefs.getString("user_token", null);
        MyTripsViewModel myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);

        myTripsViewModel.getUser(user_token).observe(this, user -> {

            if (user.getTrips().size() > 0 &&user.getTrips().get(trip_index).getDestinations().size() > 0) {

                List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                user.getTrips().get(trip_index).getDestinations().forEach(des -> {

                    MarkerOptions m = new MarkerOptions()
                            .position(new LatLng(des.getLat(), des.getLng()))
                            .title(des.getAddress());
                    markers.add(m);
                    googleMap.addMarker(m);

                });


                LatLng position;
                for (int i = 0; i < markers.size(); i++) {
                    position = markers.get(i).getPosition();
                    builder.include(new LatLng(position.latitude, position.longitude));
                }
                LatLngBounds bounds = builder.build();

                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
//            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            }


        });


    }

    public View popDestinationDialog(View view) {


        DestinationDialogFragment fragment = DestinationDialogFragment.newInstance("dialog", "1");
        fragment.show(getSupportFragmentManager(), "myAlert");
        return view;
    }
}