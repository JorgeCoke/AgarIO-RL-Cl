package com.cokejorge.es.imaginechallenge;

/**
 * Created by cokelas on 4/11/16.
 */

public class User {

    private int type; //Tipo de usuario (0 = coche, 1 = bicicleta, 2 = peaton)
    private double[] location; 	//Indexamos BD por localicacion
    int weight;

    public User(){

    }

    public User (int type, double[] location, int weight){
        this.type = type;
        this.location = location;
        this.weight = weight;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation (double [] location){
        this.location = location;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "User(type = "+type+", location = " + location[0] + "-" + location[1] + ", weight = "+weight+")";
    }

}
