package com.example.datensammler17.Gyro;

/**
 * Created by J.Kost on 09.11.2016.
 */

public class Gyro {
    private int id;
    private double x;
    private double y;
    private double z;

    public Gyro(){}

    public Gyro(int id, double x, double y, double z){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
