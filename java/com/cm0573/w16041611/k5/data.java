package com.cm0573.w16041611.k5;

public class data{
    public String id;
    public String address;
    public String image;
    public String name;
    public String number;
    public String open;
    public String type;




    public data(){}

    public data(String id, String address, String image, String name, String number, String open, String type){
        this.id = id;
        this.address = address;
        this.image = image;
        this.name = name;
        this.number = number;
        this.open = open;
        this.type = type;
    }
    public String getId(){ return id;}
    public String getAddress(){ return address;}
    public String getImage(){ return image;}
    public String getName1(){ return name;}
    public String getNumber(){ return number;}
    public String getOpen(){ return open;}
    public String getType(){ return type;}
}
