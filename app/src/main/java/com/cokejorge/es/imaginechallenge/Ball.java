package com.cokejorge.es.imaginechallenge;

/**
 * Created by cokelas on 4/11/16.
 */

public class Ball {

    private double[] location; 	//Indexamos BD por localicacion
    private Bank bank;

    public Ball(){

    }

    public Ball( double[] location, Bank bank){
        this.location = location;
        this.bank = bank;
        bank.setNumBalls(bank.getNumBalls()+1);
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation (double [] location){
        this.location = location;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "Localizable(location = " + location[0] + "-" + location[1] + ")";
    }

}
