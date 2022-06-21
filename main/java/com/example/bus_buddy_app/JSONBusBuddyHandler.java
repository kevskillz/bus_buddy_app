package com.example.bus_buddy_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JSONBusBuddyHandler {
    @GET("bus_buddy_student")
    Call<List<Student>> getStudents();

    @GET("bus_buddy_driver")
    Call<List<Driver>> getDrivers();

    @GET("bus_buddy_time_logs")
    Call<List<LogData>> getTimeLogs();

    @GET("bus_buddy_get_messages")
    Call<List<Message>> getMessages();

    @POST("bus_buddy_send_message")
    Call<Message> sendMessage(@Body Message body);
}
