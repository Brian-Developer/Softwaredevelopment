package com.cm0573.w16041611.k5;

public class userdata {
    public String id;
    public String username;
    public String password;
    public String number;

    public userdata(){
    }
    public userdata(String id, String username, String password,String number){
        this.id = id;
        this.username = username;
        this.password = password;
        this.number = number;
    }

    public String getid(){ return id;}
    public String getUsername(){ return username;}
    public String getPassword(){ return password;}
    public String getNumber(){ return number;}
}
