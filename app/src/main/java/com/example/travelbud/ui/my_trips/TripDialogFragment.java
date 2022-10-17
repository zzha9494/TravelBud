package com.example.travelbud.ui.my_trips;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelbud.FirebaseUtils;
import com.example.travelbud.R;
import com.example.travelbud.RegisterActivity;
import com.example.travelbud.TravelBudUser;
import com.example.travelbud.Trip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TripDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripDialogFragment newInstance(String param1, String param2) {
        TripDialogFragment fragment = new TripDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_dialog, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_trip_dialog, null))
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences prefs = getActivity().getSharedPreferences("user_token"
                                , Context.MODE_PRIVATE);
                        String user_token = prefs.getString("user_token", null);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                        MyTripsViewModel myTripsViewModel =
                                new ViewModelProvider(getActivity()).get(MyTripsViewModel.class);

                        myTripsViewModel.getUser(user_token).observe(getViewLifecycleOwner(),
                                user -> {
                                    TravelBudUser temp = user;
                                    Trip trip = new Trip();


                                    EditText editText =
                                            (EditText) getDialog().findViewById(R.id.input_trip_name);


                                    Log.i("MES", editText.getText().toString());
                                    if (!"".equals(editText.getText().toString().trim())) {
                                        trip.setName(editText.getText().toString());
                                        List<Trip> trips = user.getTrips();
                                        trips.add(trip);
                                        temp.setTrips(trips);
                                        FirebaseUtils.update(mDatabase, temp);
                                    } else {
                                        Toast.makeText(getActivity(), "Invalid trip name!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                });

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TripDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}