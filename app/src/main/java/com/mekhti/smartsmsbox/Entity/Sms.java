package com.mekhti.smartsmsbox.Entity;

import android.location.Location;

import java.util.Date;

public class Sms {

    private String sender;
    private String message;
    private Location location;
    private Date date;
    private SmsTypes type;

    public Sms(String sender , String message){
        this.sender = sender;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SmsTypes getType() {
        return type;
    }

    public void setType(SmsTypes type) {
        this.type = type;
    }
}
