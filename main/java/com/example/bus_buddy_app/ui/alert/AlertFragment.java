package com.example.bus_buddy_app.ui.alert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.example.bus_buddy_app.Database;
import com.example.bus_buddy_app.MainActivity;
import com.example.bus_buddy_app.Message;
import com.example.bus_buddy_app.R;
import com.example.bus_buddy_app.Student;
import com.example.bus_buddy_app.databinding.FragmentAlertBinding;

import com.example.bus_buddy_app.ui.home.HomeFragment;
import com.example.bus_buddy_app.ui.logs.LogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlertFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String[] items = new String[]{"AM", "PM"};
    private static String ampm = "AM";
    private static String bus_num;
    private static Context c;

    @SuppressLint("SetTextI18n")
    public static void updateMessages() {
        binding.textFeedback.setText("");
        binding.buttonSyncAlerts.setVisibility(View.VISIBLE);
        binding.tableMessages.setVisibility(View.VISIBLE);
        binding.tableMessages.removeAllViewsInLayout();
        ArrayList<Message> msgArr = Database.getMessages();
        for (Message msg : msgArr) {
            Activity activity = (Activity) binding.getRoot().getContext();
            TableRow name_and_bus_num = new TableRow(activity);
            TableRow message = new TableRow(activity);
            TextView tv_name_and_bus_num = new TextView(activity);
            tv_name_and_bus_num.setText(" " + msg.getName() + " - " + msg.getAm_or_pm().toUpperCase() + " Bus #" + msg.getTarget_bus());
            TextView tv_message = new TextView(activity);
            tv_message.setText(" " + msg.getMessage());

            LogFragment.setTextViewStyle(tv_message);
            tv_name_and_bus_num.setTextColor(ContextCompat.getColor(c, R.color.log_text));


            final TableRow pad_row = new TableRow(activity);
            pad_row.setBackgroundResource(R.color.background);

            final TableRow pad_row2 = new TableRow(activity);
            pad_row2.setBackgroundResource(R.color.background);

            pad_row.setDividerPadding(5);
            pad_row2.setDividerPadding(5);

            TextView empty1 = new TextView(activity);
            TextView empty2 = new TextView(activity);

            pad_row.addView(empty1);
            pad_row2.addView(empty2);

            message.addView(tv_message);
            name_and_bus_num.addView(tv_name_and_bus_num);


            LogFragment.setSubRowStyle(message);
            name_and_bus_num.setBackgroundResource(R.color.log_header);

            binding.tableMessages.addView(name_and_bus_num);
            binding.tableMessages.addView(message);
            binding.tableMessages.addView(pad_row);
            binding.tableMessages.addView(pad_row2);
        }
    }

    String message = "";

    private static FragmentAlertBinding binding;

    @SuppressLint("SetTextI18n")
    public static void messageSent() {
        binding.textFeedback.setText("Message Sent!");
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AlertViewModel homeViewModel =
                new ViewModelProvider(this).get(AlertViewModel.class);

        binding = FragmentAlertBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        c = getContext();
        //get the spinner from the xml.
        Spinner dropdown = binding.dropdownAmPm;
        //create a list of items for the spinner.

        ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        binding.textFeedback.setTextColor(ContextCompat.getColor(c, R.color.black));
        binding.textFeedback.setText("");

        Button bSendAlert = binding.buttonSendAlert;

        bSendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = binding.textAlert.getText().toString();

                Database.postMessage(new Message(
                        Database.loggedInDriver.getName(),
                        message,
                        bus_num,
                        ampm));
            }

        });

        if (HomeFragment.loggedIn) {
            if (HomeFragment.driverAccount) {

                bus_num = Database.loggedInDriver.getBus_am();

//                ((RelativeLayout.LayoutParams) binding.textFeedback.getLayoutParams())
//                        .addRule(RelativeLayout.BELOW, R.id.button_send_alert);

                binding.tableMessages.setVisibility(View.INVISIBLE);
                binding.buttonSyncAlerts.setVisibility(View.INVISIBLE);


                binding.textAlert.setVisibility(View.VISIBLE);
                binding.alertTitle.setVisibility(View.VISIBLE);
                binding.buttonSendAlert.setVisibility(View.VISIBLE);
                binding.dropdownAmPm.setVisibility(View.VISIBLE);



            } else {


                ((RelativeLayout.LayoutParams) binding.textFeedback.getLayoutParams())
                        .addRule(RelativeLayout.BELOW, R.id.table_messages);

                binding.textFeedback.setText("Getting messages...");

                binding.textAlert.setVisibility(View.INVISIBLE);
                binding.alertTitle.setVisibility(View.INVISIBLE);
                binding.buttonSendAlert.setVisibility(View.INVISIBLE);
                binding.dropdownAmPm.setVisibility(View.INVISIBLE);

                binding.tableMessages.setVisibility(View.VISIBLE);



                Database.syncMessages();


            }
        }
        else {

            binding.textAlert.setVisibility(View.INVISIBLE);
            binding.alertTitle.setVisibility(View.INVISIBLE);
            binding.buttonSendAlert.setVisibility(View.INVISIBLE);
            binding.dropdownAmPm.setVisibility(View.INVISIBLE);
            binding.tableMessages.setVisibility(View.INVISIBLE);
            binding.buttonSyncAlerts.setVisibility(View.INVISIBLE);

            binding.textFeedback.setTextColor(ContextCompat.getColor(c, R.color.red));
            binding.textFeedback.setText(R.string.not_logged_in);

        }


        FloatingActionButton bSync = binding.buttonSyncAlerts;
        bSync.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                ((RelativeLayout.LayoutParams) binding.textFeedback.getLayoutParams())
                        .addRule(RelativeLayout.BELOW, R.id.table_messages);

                binding.textFeedback.setText("Getting messages...");

                binding.textAlert.setVisibility(View.INVISIBLE);
                binding.buttonSyncAlerts.setVisibility(View.INVISIBLE);
                binding.alertTitle.setVisibility(View.INVISIBLE);
                binding.buttonSendAlert.setVisibility(View.INVISIBLE);
                binding.dropdownAmPm.setVisibility(View.INVISIBLE);
                binding.tableMessages.setVisibility(View.INVISIBLE);


                Database.syncMessages();

            }

        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (i) {
                case 0:
                    ampm = "AM";
                    bus_num = Database.loggedInDriver.getBus_am();
                    break;
                case 1:
                    ampm = "PM";
                    bus_num = Database.loggedInDriver.getBus_pm();

                    break;
            }
        }
        catch (NullPointerException ignore) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}