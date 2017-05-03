package com.example.datensammler17.Light;

/**
 * Created by J.Kost on 09.11.2016.
 */

public class Light {
    private int id;
    private double lux;

    public Light(){
    }

    public Light(int id, double lux){
        this.id = id;
        this.lux = lux;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLux() {
        return lux;
    }

    public void setLux(double lux) {
        this.lux = lux;
    }
}
