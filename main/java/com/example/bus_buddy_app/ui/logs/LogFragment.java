package com.example.bus_buddy_app.ui.logs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import com.example.bus_buddy_app.Database;

import com.example.bus_buddy_app.LogData;
import com.example.bus_buddy_app.LogPair;
import com.example.bus_buddy_app.MainActivity;
import com.example.bus_buddy_app.R;
import com.example.bus_buddy_app.databinding.FragmentLogsBinding;
import com.example.bus_buddy_app.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class LogFragment extends Fragment {

private static FragmentLogsBinding binding;

    private static Context c;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        LogViewModel homeViewModel =
                new ViewModelProvider(this).get(LogViewModel.class);

        binding = FragmentLogsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        c = getContext();

        FloatingActionButton bSync = binding.buttonSyncLogs;
        bSync.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                    binding.tableLogs.setVisibility(View.INVISIBLE);
                binding.buttonSyncLogs.setVisibility(View.INVISIBLE);

                binding.textLogs.setTextColor(ContextCompat.getColor(c, R.color.purple_200));
                    binding.textLogs.setText("Getting logs...");
                    Database.syncLogData();

            }

        });





        if (HomeFragment.loggedIn && !HomeFragment.driverAccount) {

            binding.buttonSyncLogs.setVisibility(View.VISIBLE);
            binding.textLogs.setTextColor(ContextCompat.getColor(c, R.color.purple_200));
            binding.textLogs.setText("Getting logs...");
            Database.syncLogData();


        } else {
            binding.buttonSyncLogs.setVisibility(View.INVISIBLE);
            binding.textLogs.setTextColor(ContextCompat.getColor(c, R.color.red));
            binding.textLogs.setText(R.string.not_logged_in);


        }

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateLogs() {
        binding.textLogs.setText("");
        binding.buttonSyncLogs.setVisibility(View.VISIBLE);
        binding.tableLogs.removeAllViewsInLayout();


        ArrayList<LogData> logs = Database.getLogs();

        ArrayList<LogPair> dT = new ArrayList<>();

        final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        for (LogData log : logs) {
            String on_time = log.getOn_time();
            String off_time = log.getOff_time();


            dT.add(new LogPair(on_time,
                    off_time,
                    format,
                    off_time.equals("Null")));
        }

        Collections.sort(dT);





        for (LogPair logPair : dT) {
            String date = logPair.date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            String on = logPair.on.format(DateTimeFormatter.ofPattern("HH:mm"));
            String off;
            if (!logPair.isOffNull) {
                off = logPair.off.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            else {
                off = "Not Signed Off";
            }




            Activity activity = (Activity) binding.getRoot().getContext();
            TableRow date_row = new TableRow(activity);
            TableRow on_row = new TableRow(activity);
            TableRow off_row = new TableRow(activity);


            final TableRow pad_row = new TableRow(activity);
            pad_row.setBackgroundResource(R.color.background);

            final TableRow pad_row2 = new TableRow(activity);
            pad_row2.setBackgroundResource(R.color.background);

            pad_row.setDividerPadding(5);
            pad_row2.setDividerPadding(5);

            TextView empty1 = new TextView(activity);
            TextView empty2 = new TextView(activity);




            TextView tv_on_field = new TextView(activity);
            tv_on_field.setText(" On Bus Time");
            TextView tv_off_field = new TextView(activity);
            tv_off_field.setText(" Off Bus Time");

            TextView tv_date = new TextView(activity);
            tv_date.setText(" " + date);



            TextView tv_on = new TextView(activity);
            tv_on.setText(on);

            TextView tv_off = new TextView(activity);
            tv_off.setText(off);

            setTextViewStyle(tv_on_field, tv_off_field, tv_off, tv_on, empty1, empty2);
            tv_date.setTextColor(ContextCompat.getColor(c, R.color.log_text));

            date_row.addView(tv_date);
            on_row.addView(tv_on_field);
            on_row.addView(tv_on);
            off_row.addView(tv_off_field);
            off_row.addView(tv_off);

            date_row.setBackgroundResource(R.color.log_header);

            setSubRowStyle(on_row, off_row);

            pad_row.addView(empty1);
            pad_row2.addView(empty2);


            binding.tableLogs.addView(date_row);
            binding.tableLogs.addView(on_row);
            binding.tableLogs.addView(off_row);
            binding.tableLogs.addView(pad_row);
            binding.tableLogs.addView(pad_row2);

            binding.tableLogs.setVisibility(View.VISIBLE);


        }



    }

    @SuppressLint("ResourceAsColor")
    public static void setSubRowStyle(TableRow... rows) {
        for (TableRow r : rows) {
            r.setBackgroundResource(R.color.log_datapoint);
            r.setDividerPadding(5);

        }
    }

    @SuppressLint("ResourceAsColor")
    public static void setTextViewStyle(TextView... tvs) {
        for (TextView tv : tvs) {
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f));


        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onResume() {
        super.onResume();

    }
}