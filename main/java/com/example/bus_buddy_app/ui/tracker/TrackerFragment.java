package com.example.bus_buddy_app.ui.tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_buddy_app.Database;
import com.example.bus_buddy_app.R;
import com.example.bus_buddy_app.databinding.FragmentLogsBinding;
import com.example.bus_buddy_app.databinding.FragmentTrackerBinding;

import com.example.bus_buddy_app.ui.home.HomeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class TrackerFragment extends Fragment {

    private static final int MAP_PADDING = 120;
    private static FragmentTrackerBinding binding;
    private static String last_dist;
    private static int last_secs_away;




    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TrackerViewModel homeViewModel =
                new ViewModelProvider(this).get(TrackerViewModel.class);

        binding = FragmentTrackerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded


                    if (HomeFragment.loggedIn) {


                        MarkerOptions user_marker = new MarkerOptions();

                        LatLng userLatLng = new LatLng(Database.home_Lat, Database.home_Long);

                        user_marker.position(userLatLng);

                        user_marker.title("Your Location!");



                        googleMap.clear();

                        LatLng busLatLng = new LatLng(Database.sample_Lat, Database.sample_Long);
                        MarkerOptions bus_marker = new MarkerOptions();


                        bus_marker.position(busLatLng);

                        bus_marker.title("Bus Location!");



                        googleMap.addMarker(user_marker);
                        googleMap.addMarker(bus_marker);


                        LatLngBounds.Builder b = new LatLngBounds.Builder();

                        b.include(user_marker.getPosition());
                        b.include(new LatLng(Database.sample_Lat, Database.sample_Long));

                        LatLngBounds bounds = b.build();

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                                bounds,

                                MAP_PADDING
                        ));


                    }

                }

        });

        if (HomeFragment.loggedIn) {
            binding.textTracker.setTextColor(R.color.black);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.SECOND, last_secs_away);
            @SuppressLint("SimpleDateFormat") String now = new SimpleDateFormat("HH:mm aa")
                    .format(c.getTime());

            binding.textTracker.setText(last_dist + " away\n EST: " + now);
        } else {
            binding.textTracker.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            binding.textTracker.setText(R.string.not_logged_in);
        }
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    public static void updateDistance(String drivDist, int secsAway) {
        try {
            last_dist = drivDist;
            last_secs_away = secsAway;
            if (HomeFragment.loggedIn) {
                binding.textTracker.setTextColor(R.color.black);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, last_secs_away);
                @SuppressLint("SimpleDateFormat") String now = new SimpleDateFormat("HH:mm aa")
                        .format(c.getTime());

                binding.textTracker.setText(last_dist + " away\n EST: " + now);
            }
        }
        catch (NullPointerException ignored) {

        }
    }


}