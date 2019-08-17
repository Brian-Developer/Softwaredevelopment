package com.cm0573.w16041611.k5;

public class booking {
    public String idbooking;
    public String email;
    public String restaurant;
    public String time;
    public String date;
    public String contact;
    public String guest;
    public String note;
    public String address;
    public String image;

    public booking(){

    }

    public booking(String idbooking, String email, String restaurant, String time, String date, String contact, String guest, String note, String address, String image) {
        this.idbooking = idbooking;
        this.email = email;
        this.restaurant = restaurant;
        this.time = time;
        this.date = date;
        this.contact = contact;
        this.guest = guest;
        this.note = note;
        this.address = address;
        this.image = image;
    }
    public String getIdbooking() {
        return idbooking;
    }

    public String getEmail() {
        return email;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getContact() {
        return contact;
    }

    public String getGuest() {
        return guest;
    }

    public String getNote() {
        return note;
    }

    public String getImage() {
        return image;
    }
}
