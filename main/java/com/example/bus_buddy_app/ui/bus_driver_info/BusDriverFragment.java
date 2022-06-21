package com.example.bus_buddy_app.ui.bus_driver_info;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bus_buddy_app.Database;
import com.example.bus_buddy_app.Driver;
import com.example.bus_buddy_app.ImageLoadTask;
import com.example.bus_buddy_app.MainActivity;
import com.example.bus_buddy_app.R;
import com.example.bus_buddy_app.databinding.FragmentDriverInfoBinding;
import com.example.bus_buddy_app.ui.home.HomeFragment;
import com.example.bus_buddy_app.ui.tracker.TrackerFragment;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class BusDriverFragment extends Fragment {

private FragmentDriverInfoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        BusDriverViewModel notificationsViewModel =
                new ViewModelProvider(this).get(BusDriverViewModel.class);

        binding = FragmentDriverInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (HomeFragment.loggedIn && !HomeFragment.driverAccount) {
            Driver drivAM = Database.getDriverAM();
            Driver drivPM = Database.getDriverPM();




            new ImageLoadTask(drivAM.getHeadshot(), binding.imageHeadshotAM).execute();
            new ImageLoadTask(drivPM.getHeadshot(), binding.imageHeadshotPM).execute();







            binding.textBusAMEmail.setText(drivAM.getEmail());
            binding.textBusAMName.setText(drivAM.getName());
            binding.textBusAMPhone.setText(drivAM.getPhone());
            binding.textBusAMNumber.setText(drivAM.getBus_am());

            binding.textBusPMEmail.setText(drivPM.getEmail());
            binding.textBusPMName.setText(drivPM.getName());
            binding.textBusPMPhone.setText(drivPM.getPhone());
            binding.textBusPMNumber.setText(drivPM.getBus_pm());


        } else {

            binding.tableBusAM.setVisibility(View.INVISIBLE);
            binding.tableBusPM.setVisibility(View.INVISIBLE);


            binding.textDriverInfoLoggedIn.setText(R.string.not_logged_in);

        }



        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}