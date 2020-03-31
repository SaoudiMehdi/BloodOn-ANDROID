package com.example.bloodon;

public class Hopital {
    String name;
    String address;
    Stock stock;
    int img;
    public Hopital(String name, String address, int img){
        this.name=name;
        this.address = address;
        this.img=img;
    }

    //Getter

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
    public int getImg(){
        return img;
    }



    //setter

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setImg(int img){
        this.img=img;
    }

}
