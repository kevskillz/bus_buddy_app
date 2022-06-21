package com.example.bus_buddy_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.bus_buddy_app.ui.alert.AlertFragment;
import com.example.bus_buddy_app.ui.home.HomeFragment;
import com.example.bus_buddy_app.ui.logs.LogFragment;
import com.example.bus_buddy_app.ui.tracker.TrackerFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Database {




    final static String TAG = "DATABASE";
    private static Student mStudent = null;
    private static Driver mDriverAM = null;
    private static Driver mDriverPM = null;
    private static List<Student> students;
    private static List<Message> messages;
    private static List<Driver> drivers;
    public static Driver loggedInDriver;
    private static List<LogData> logs;
    private static OkHttpClient okHttpClient;
    private final Retrofit mongoRetrofit;
    private static JSONBusBuddyHandler jsonBusBuddyHandler;

    public static double sample_Lat = 40.157850, sample_Long = -75.271650; // bus yard
    public static double home_Lat = 40.171100, home_Long = -75.228390; // school



    public static void updateDrivingDistance(double latFrom, double lngFrom, double latTo, double lngTo) {
        final String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                latFrom + "," + lngFrom + "&destination=" + latTo + "," + lngTo +
                "&key=" + BuildConfig.MAPS_API_KEY;


        Request r = new Request.Builder()
                .url(url)
                .build();

        okhttp3.Call c = okHttpClient.newCall(r);
        c.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String jsonString = response.body().string();

                JSONObject obj = null;
                try {
                    obj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject j = obj
                            .getJSONArray("routes")
                            .getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONObject("duration");

                    String distStr = j.getString("text");
                    int secondsAway = j.getInt("value");


                    MainActivity.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            HomeFragment.updateDistance(distStr);
                            TrackerFragment.updateDistance(distStr, secondsAway);
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    @SuppressLint("MissingPermission")
    public Database() {


        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        mongoRetrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/app/bus_buddy_app-swldj/endpoint/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonBusBuddyHandler = mongoRetrofit.create(JSONBusBuddyHandler.class);

        updateDrivingDistance(home_Lat, home_Long, sample_Lat, sample_Long);

        initStudentData();
        initDriverData();

    }

    public static Student getStudent() {
        return mStudent;
    }

    public static Student getStudent(String name, String school) {

        for (Student student : students) {
            if (student.getName().trim().toLowerCase().equals(name) && student.getSchool().trim().toLowerCase().equals(school)) {
                mStudent = student;

            }
        }



        return mStudent;
    }


    @SuppressLint("NewApi")
    public static ArrayList<LogData> getLogs() {
        ArrayList<LogData> logArr = new ArrayList<>();

        for (LogData log : logs) {
            if (mStudent.getId().equals(log.getId())) {
                logArr.add(log);
            }
        }



        return logArr;
    }

    public static ArrayList<Message> getMessages() {
        ArrayList<Message> msgArr = new ArrayList<>();

        for (Message msg : messages) {
            String stud_num;
            if (msg.getAm_or_pm().toLowerCase().equals("am")) {
                stud_num = mStudent.getBus_am();
            } else {
                stud_num = mStudent.getBus_pm();
            }

            if (stud_num.equals(msg.getTarget_bus())) {
                msgArr.add(msg);
            }
        }

        return msgArr;
    }


    public static Driver getDriver(String name, String email) {


        for (Driver driver : drivers) {
            if (driver.getName().trim().toLowerCase().equals(name) && driver.getEmail().trim().toLowerCase().equals(email)) {
                loggedInDriver = driver;
                return driver;

            }
        }



        return null;
    }


    public static Driver getDriverAM() {

        for (Driver driver : drivers) {
            if (driver.getBus_am().equals(mStudent.getBus_am())) {
                mDriverAM = driver;
            } else if (driver.getBus_pm().equals(mStudent.getBus_pm())) {
                mDriverPM = driver;
            }
        }
        return mDriverAM;
    }

    public static Driver getDriverPM() {

        for (Driver driver : drivers) {
            if (driver.getBus_am().equals(mStudent.getBus_am())) {
                mDriverAM = driver;
            }
            if (driver.getBus_pm().equals(mStudent.getBus_pm())) {
                mDriverPM = driver;
            }
        }
        return mDriverPM;
    }



    private void initStudentData() {
        Call<List<Student>> call = jsonBusBuddyHandler.getStudents();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                students = response.body();

                assert students != null;

            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.toString());
            }
        });
    }   

    private static void initDriverData() {
        Call<List<Driver>> call = jsonBusBuddyHandler.getDrivers();
        call.enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                drivers = response.body();

                assert drivers != null;


                Log.d(TAG, drivers.get(0).getName());
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void syncLogData() {
        Call<List<LogData>> call = jsonBusBuddyHandler.getTimeLogs();
        call.enqueue(new Callback<List<LogData>>() {
            @Override
            public void onResponse(Call<List<LogData>> call, Response<List<LogData>> response) {
                logs = response.body();

                assert logs != null;

                LogFragment.updateLogs();
            }

            @Override
            public void onFailure(Call<List<LogData>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.toString());
            }
        });


    }

    public static void syncMessages() {
        Call<List<Message>> call = jsonBusBuddyHandler.getMessages();
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messages = response.body();

                assert messages != null;

                AlertFragment.updateMessages();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.toString());
            }
        });
    }

    public static void postMessage(Message msg) {
        Call<Message> call = jsonBusBuddyHandler.sendMessage(msg);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                assert  response.body() != null;

                AlertFragment.messageSent();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e(TAG, "Error: " + t.toString());
            }
        });

    }




}
