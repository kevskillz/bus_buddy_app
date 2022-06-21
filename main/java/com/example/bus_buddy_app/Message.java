package com.example.bus_buddy_app;

public class Message {

    private String name, message, target_bus, am_or_pm;

    public Message(String name, String message, String target_bus, String am_or_pm) {
        this.name = name;
        this.message = message;
        this.target_bus = target_bus;
        this.am_or_pm = am_or_pm;
    }


    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTarget_bus() {
        return target_bus;
    }

    public String getAm_or_pm() {
        return am_or_pm;
    }
}
