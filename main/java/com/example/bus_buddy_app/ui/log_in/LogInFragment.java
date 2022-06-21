package com.example.bus_buddy_app.ui.log_in;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.example.bus_buddy_app.Database;
import com.example.bus_buddy_app.Driver;
import com.example.bus_buddy_app.MainActivity;
import com.example.bus_buddy_app.R;
import com.example.bus_buddy_app.Student;
import com.example.bus_buddy_app.databinding.FragmentLogInBinding;
import com.example.bus_buddy_app.ui.home.HomeFragment;

public class LogInFragment extends Fragment {

private FragmentLogInBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        LogInViewModel homeViewModel =
                new ViewModelProvider(this).get(LogInViewModel.class);

        binding = FragmentLogInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button bLogIn = binding.buttonLogIn;

        bLogIn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Student student = null;
                Driver driver = null;
                if (!HomeFragment.driverAccount) {
                    student = Database.getStudent(binding.textName.getText().toString().trim().toLowerCase(),
                            binding.textSchoolDistrict.getText().toString().trim().toLowerCase());
                    if (student == null) {
                        binding.textError.setText("Error Logging In");
                        return;
                    }
                } else {
                    driver = Database.getDriver(binding.textName.getText().toString().trim().toLowerCase(),
                            binding.textSchoolDistrict.getText().toString().trim().toLowerCase());
                    if (driver == null) {
                        binding.textError.setText("Error Logging In");
                        return;
                    }
                }



                binding.textError.setText("");
                HomeFragment.loggedIn = true;
//                Database.syncLogData();


                NavDirections n = new NavDirections() {
                    @Override
                    public int getActionId() {

                        return R.id.action_navigation_log_in_to_navigation_home;
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

        CheckBox cDriver = binding.switchDriverAccount;

        cDriver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HomeFragment.driverAccount = !HomeFragment.driverAccount;
                if (HomeFragment.driverAccount) {
                    binding.textSchoolDistrict.setHint(R.string.email);
                } else {
                    binding.textSchoolDistrict.setHint(R.string.school_district);
                }
            }

        });


        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}