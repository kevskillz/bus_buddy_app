package com.example.bus_buddy_app.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bus_buddy_app.Database;
import com.example.bus_buddy_app.MainActivity;
import com.example.bus_buddy_app.R;
import com.example.bus_buddy_app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements Runnable {


    private static FragmentHomeBinding binding;


    private static LocationManager locationManager;
    public static boolean loggedIn = false;
    public static boolean driverAccount = false;
    private Button bLogIn;
    private static String last_dist;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        Button bTrackKid = binding.buttonTrackYourKid;


        bTrackKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections n = new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_navigation_home_to_navigation_tracker;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                };
                MainActivity.navController.navigate(n);

//                MainActivity.navController.popBackStack();
            }

        });


        bLogIn = binding.buttonLogInHome;


        bLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections n;
                if (!loggedIn) {

                    n = new NavDirections() {
                        @Override
                        public int getActionId() {
                            return R.id.action_navigation_home_to_navigation_log_in;
                        }

                        @NonNull
                        @Override
                        public Bundle getArguments() {
                            return null;
                        }
                    };

//                MainActivity.navController.popBackStack();
                } else {
                    n = new NavDirections() {
                        @Override
                        public int getActionId() {
                            return R.id.action_navigation_home_to_navigation_user_info;
                        }

                        @NonNull
                        @Override
                        public Bundle getArguments() {
                            return null;
                        }
                    };
                }
                MainActivity.navController.navigate(n);
            }

        });

        if (loggedIn) {
            bLogIn.setText("");
            bLogIn.setBackgroundResource(R.drawable.avatar);
            updateDistance(last_dist);
        }

        if (HomeFragment.loggedIn && !driverAccount) binding.textArrival.setText(R.string.title_arriving);
        else {
            binding.textTime.setText("");
            binding.textArrival.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            binding.textArrival.setText(R.string.not_logged_in_home);
        }






        return root;
    }


    public static void updateDistance(String drivDist) {
        last_dist = drivDist;
        if (loggedIn) {
            try {
                binding.textTime.setText(drivDist);
            }
            catch (NullPointerException e) {
                Log.e("err", e.toString());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void run() {

    }
}