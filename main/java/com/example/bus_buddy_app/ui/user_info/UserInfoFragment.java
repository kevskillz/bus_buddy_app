package com.example.bus_buddy_app.ui.user_info;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.bus_buddy_app.databinding.FragmentUserInfoBinding;
import com.example.bus_buddy_app.ui.home.HomeFragment;

public class UserInfoFragment extends Fragment {

private FragmentUserInfoBinding binding;




    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        UserInfoViewModel homeViewModel =
                new ViewModelProvider(this).get(UserInfoViewModel.class);

        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button bHome = binding.buttonHome;
        bHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections n = new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_navigation_user_info_to_navigation_home;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                };
                MainActivity.navController.navigate(n);


            }

        });

        Button bLogOut = binding.buttonLogOut;
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.loggedIn = false;
                HomeFragment.driverAccount = false;
                NavDirections n = new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_navigation_user_info_to_navigation_log_in;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                };
                MainActivity.navController.navigate(n);

            }

        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        if (!HomeFragment.driverAccount) {
            Student student = Database.getStudent();
            binding.nameUserInfo.setText(student.getName());
            binding.schoolTag.setText("School");
            binding.schoolUserInfo.setText(student.getSchool());
            binding.amBusUserInfo.setText(student.getBus_am());
            binding.pmBusUserInfo.setText(student.getBus_pm());
        } else {
            Driver driver = Database.loggedInDriver;
            binding.nameUserInfo.setText(driver.getName());
            binding.schoolTag.setText("Email");
            binding.schoolUserInfo.setText(driver.getEmail());
            binding.amBusUserInfo.setText(driver.getBus_am());
            binding.pmBusUserInfo.setText(driver.getBus_pm());
        }
    }

}