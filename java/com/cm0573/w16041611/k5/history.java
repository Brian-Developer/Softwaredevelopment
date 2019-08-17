package com.cm0573.w16041611.k5;

public class history {
    public String idbooking;
    public String contact;
    public String date;
    public String email;
    public String guest;
    public String note;
    public String restaurant;
    public String time;
    public String address;
    public String image;

    public history(){}

    public history(String idbooking, String contact, String date, String email, String guest, String note, String restaurant, String time, String address, String image){
        this.idbooking = idbooking;
        this.contact = contact;
        this.date = date;
        this.email = email;
        this.guest = guest;
        this.note = note;
        this.restaurant = restaurant;
        this.time = time;
        this.address = address;
        this.image = image;
    }

    public String getIdbooking() {
        return idbooking;
    }

    public String getContact() {
        return contact;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public String getGuest() {
        return guest;
    }

    public String getNote() {
        return note;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }
}
