package com.mygdx.skyteam.logic;

import java.util.ArrayList;

/**
 * Represents an airplane with various components such as engine, landing gear,
 * brakes, and flaps.
 * Manages the state and behavior of an airplane during gameplay.
 * 
 * Coded primarily by: Marija Voloder (Class structure, constructors, function
 * definitions and variables), with a few getter methods and initializations
 * added by Rathin.
 */
public class Airplane {
    private Altitude altitude;
    private Axis axis;
    private Engine engine;
    private Concentration coffee;
    private LandingGear landingGears;
    private Flap flaps;
    private Brake brakes;
    private ArrayList<Integer> planesOnTrack;

    /**
     * Initializes a new instance of the Airplane class.
     * Sets up all the components and initializes the number of plane in each field
     * on track with default values.
     * 
     */
    public Airplane() {
        altitude = new Altitude();
        axis = new Axis();
        engine = new Engine();
        landingGears = new LandingGear();
        brakes = new Brake();
        flaps = new Flap();
        coffee = new Concentration();
        planesOnTrack = new ArrayList<>();

        planesOnTrack.add(0); // 7 fields to airport, each index is position and contains the number of planes
                              // on it, this is the original game track
        planesOnTrack.add(0);
        planesOnTrack.add(1);
        planesOnTrack.add(2);
        planesOnTrack.add(1);
        planesOnTrack.add(3);
        planesOnTrack.add(2);
    }

    /**
     * Adjusts the speed of the airplane based on the track.
     *  Written by: Marija
     */

    public void adjustSpeed() {
        engine.adjustSpeed(planesOnTrack);
    }

    /**
     * Adjusts the tilt of the airplane.
     *  Written by: Marija & Rathin
     */
    public void adjustTilt() {
        axis.adjustTilt();
    }

    /**
     * Deploys the airplane's brakes based on pilot input.
     *
     * @param pilotInput the intensity of the brake deployment.
     *  Written by: Marija & Rathin
     *                  
     */

    public void deployBrakes(int pilotInput) {
        brakes.deployBrakes(pilotInput);
    }

    /**
     * Deploys the landing gear of the airplane based on pilot's input, which
     * increases the value of blue marker
     *
     * @param pilotInput  dice value of the pilot
     * @param fieldChoice the placeholder where the landing gear will be deployed.
     *                
     * Written by: Marija & Rathin 
     */
    public void deployLandingGear(int pilotInput, int fieldChoice) {
        landingGears.deployLandingGear(pilotInput, this, fieldChoice);
    }

    /**
     * Deploys the airplane's flaps based on co-pilot input, which increases the
     * value of orange marker
     *
     * @param coPilotInput dice value of copilot chooses to put into flap's
     *                     placeholder
     * @param fieldChoice  the placeholder where the flaps will be deployed.
     * 
     * Written by: Marija & Rathin
     */
    public void deployFlaps(int coPilotInput, int fieldChoice) {
        flaps.deployFlaps(coPilotInput, this, fieldChoice);
    }

    /**
     * Fills the coffee fields
     *
     * @param diceValue the value of the dice (copilot or pilot)
     *                  Written by: Marija
     */
    public void fillCoffeeFields(int diceValue) {
        coffee.fillCoffeeFields(diceValue);
    }

    // getters and setters
    /**
     * Gets the engine of the airplane.
     *
     * @return the airplane's engine.
     *         Written by: Marija
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Gets the axis of the airplane.
     *
     * @return the airplane's axis.
     *         Written by: Marija
     */
    public Axis getAxis() {
        return axis;
    }

    /**
     * Gets the altitude of the airplane.
     *
     * @return the airplane's altitude.
     *         Written by: Marija
     */
    public Altitude getAltitude() {
        return altitude;
    }

    /**
     * Gets the landing gears of the airplane.
     *
     * @return the airplane's landing gears.
     *         Written by: Marija
     */
    public LandingGear getLandingGears() {
        return landingGears;
    }

    /**
     * Gets the brakes of the airplane.
     *
     * @return the airplane's brakes.
     *         Written by: Marija
     */
    public Brake getBrakes() {
        return brakes;
    }

    /**
     * Gets the flaps of the airplane.
     *
     * @return the airplane's flaps.
     *         Written by: Marija
     */
    public Flap getFlaps() {
        return flaps;
    }

    /**
     * Gets the coffee concentration manager of the airplane.
     *
     * @return the airplane's coffee concentration manager.
     *         Written by: Marija
     */
    public Concentration getConcentration() {
        return coffee;
    }

    /**
     * Gets the list of planes on the track.
     *
     * @return an ArrayList representing the number of planes on each track field.
     *         Written by: Marija
     */
    public ArrayList<Integer> getPlanesOnTrack() {
        return planesOnTrack;
    }

}
