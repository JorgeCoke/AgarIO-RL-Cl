package com.cokejorge.es.imaginechallenge;

/**
 * Created by cokelas on 4/11/16.
 */

public class Bank {

    private double[] location;
    private int numBalls;

    public Bank(){

    }

    public Bank(double[] location, int numBalls) {
        this.location = location;
        this.numBalls = numBalls;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public int getNumBalls() {
        return numBalls;
    }

    public void setNumBalls(int numBalls) {
        this.numBalls = numBalls;
    }
}
