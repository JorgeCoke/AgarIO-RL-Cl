package com.cokejorge.es.imaginechallenge;

/**
 * Created by cokelas on 4/11/16.
 */

public class Localizable {

    private String id;
    private int mass; //Tipo de usuario (0 = coche, 1 = bicicleta, 2 = peaton)
    private double[] location; 	//Indexamos BD por localicacion
    private String type;

    public Localizable(){

    }

    public Localizable(int mass, double[] location, String type){
        this.mass = mass;
        this.location = location;
        this.type = type;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation (double [] location){
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Localizable(type = "+mass+", location = " + location[0] + "-" + location[1] + ", type="+type+", id="+id+")";
    }

}
