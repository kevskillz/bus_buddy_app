package com.example.bus_buddy_app;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LogPair implements Comparable<LogPair> {
    public LocalDateTime on, off;
    public LocalDate date;
    public boolean isOffNull;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LogPair(String on, String off, DateTimeFormatter format, boolean isOffNull) {
        this.on = LocalDateTime.parse(on, format);
        if (!isOffNull) {
            this.off = LocalDateTime.parse(off, format);
        }
        this.isOffNull = isOffNull;
        this.date = this.on.toLocalDate();
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(LogPair o) {
        LocalDate localDate = o.date;

        if (this.date.compareTo(localDate) > 0 || this.isOffNull) {
            return -1;
        }
        else if (this.date.compareTo(localDate) < 0) {
            return 1;
        }
        return 0;
    }
}
