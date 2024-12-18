package com.mygdx.skyteam.logic;

import java.util.ArrayList;

public class Airplane {
    private Altitude altitude;
    private Axis axis;
    private Engine engine;
    private Concentration coffee;
    private LandingGear landingGears;
    private Flap flaps;
    private Brake brakes;
    private ArrayList<Integer> planesOnTrack;

    public Airplane(){
        altitude = new Altitude();
        axis =  new Axis();
        engine = new Engine();
        landingGears = new LandingGear();
        brakes = new Brake();
        flaps = new Flap();
        coffee = new Concentration();
        planesOnTrack= new ArrayList<>();

        planesOnTrack.add(0);  //7 fields to airport, each index is position and contains the number of planes on it, this is the original game track
        planesOnTrack.add(0);
        planesOnTrack.add(1);
        planesOnTrack.add(2);
        planesOnTrack.add(1);
        planesOnTrack.add(3);
        planesOnTrack.add(2);
    }

    public void adjustSpeed(){
        engine.adjustSpeed(planesOnTrack);
    }

    public void adjustTilt(){
        axis.adjustTilt();
    }

    public void deployBrakes(int pilotInput){
        brakes.deployBrakes(pilotInput);
    }

    public void deployLandingGear(int pilotInput, int fieldChoice){
        landingGears.deployLandingGear(pilotInput, this, fieldChoice);
    }

    public void deployFlaps(int coPilotInput, int fieldChoice){
        flaps.deployFlaps(coPilotInput, this, fieldChoice);
    }

    public void fillCoffeeFields(int diceValue){
        coffee.fillCoffeeFields(diceValue);
    }

    //getters and setters
    public Engine getEngine(){
        return engine;
    }
    public Axis getAxis(){
        return axis;
    }
    public Altitude getAltitude(){
        return altitude;
    }
    public LandingGear getLandingGears() { 
        return landingGears; 
    }

    public Brake getBrakes() { 
        return brakes; 
    }

    public Flap getFlaps() { 
        return flaps; 
    }

    public Concentration getConcentration(){
        return coffee;
    }
    public ArrayList<Integer> getPlanesOnTrack(){
        return planesOnTrack;
    }

   
}
