package com.mygdx.skyteam.logic;

import java.util.Arrays;
/**
 * Manages the airplane's axis tilt based on dice rolls from the pilot and co-pilot.
 * Adjusts the tilt and tracks its state during gameplay.
 */
public class Axis {
    private int currentTilt;
    private Field pilotField;
    private Field coPilotField;
    /**
     * Initializes the Axis with default tilt and dice fields for both pilot and co-pilot.
     */
    public Axis() {
        currentTilt = 3;
        this.pilotField = new Field("Pilot Axis Field", Arrays.asList(1, 2, 3, 4, 5, 6), 810, 505);
        this.coPilotField = new Field("Co-Pilot Axis Field", Arrays.asList(1, 2, 3, 4, 5, 6), 1063, 505);
    }
    /**
     * Adjusts the tilt based on the dice values placed by the pilot and co-pilot.
     * Updates the tilt and checks if it reaches critical values (0 or 6), signaling game over.
     */
    public void adjustTilt() {

        Integer pilotDice = pilotField.getPlacedDice();
        Integer coPilotDice = coPilotField.getPlacedDice();

        int difference = Math.abs(pilotDice - coPilotDice);

        if (pilotDice > coPilotDice) {
            currentTilt -= difference;
        } else if (coPilotDice > pilotDice) {
            currentTilt += difference;
        }

        // Ensure tilt stays within bounds (0 to 6)
        if (currentTilt < 1) {
            currentTilt = 0;
        } else if (currentTilt > 5) {
            currentTilt = 6;
        }
        // Check game over conditions
        if (currentTilt == 0 || currentTilt == 6) {
            System.out.println("Plane tilted 90 degrees. GAME OVER");
        } else {
            System.out.println("Axis adjusted. Current tilt: " + currentTilt);
        }
    }
    /**
     * Places a dice value in the pilot's field.
     *
     * @param diceValue the dice value to place.
     * @throws IllegalArgumentException if the dice value is invalid for the pilot's field.
     */
    public void placePilotDice(int diceValue) {
        if (!pilotField.setDiceValue(diceValue)) {
            throw new IllegalArgumentException("Not a valid dice input for Pilot Axis Field");
        }
    }

    /**
     * Places a dice value in the co-pilot's field.
     *
     * @param diceValue the dice value to place.
     * @throws IllegalArgumentException if the dice value is invalid for the co-pilot's field.
     */
    public void placeCoPilotDice(int diceValue) {
        if (!coPilotField.setDiceValue(diceValue)) {
            throw new IllegalArgumentException("Not a valid dice input for Co-Pilot Axis Field");
        }
    }
    /**
     * Gets the pilot's axis field.
     *
     * @return the pilot's axis field.
     */
    public Field getPilotAxisField() {
        return pilotField;
    }

    /**
     * Gets the co-pilot's axis field.
     *
     * @return the co-pilot's axis field.
     */
    public Field getCoPilotAxisField() {
        return coPilotField;
    }

    /**
     * Gets the current tilt value.
     *
     * @return the current tilt.
     */
    public int getCurrentTilt() {
        return currentTilt;
    }

    /**
     * Resets both the pilot's and co-pilot's fields.
     */
    public void resetFields() {
        pilotField.resetField();
        coPilotField.resetField();
    }

    /**
     * Gets the rotation angle of the airplane based on the current tilt.
     *
     * @return the rotation angle in degrees.
     */
    public float getRotationAngle() {
        switch (currentTilt) {
            case 0:
                return 80f;
            case 1:
                return 60f;
            case 2:
                return 30f;
            case 3:
                return 0f;
            case 4:
                return -30f;
            case 5:
                return -60f;
            case 6:
                return -80f;
            default:
                return 0f;
        }
    }
    /**
     * Sets the current tilt to a specific value.
     *
     * @param tilt the new tilt value.
     */
    public void setTilt(int tilt){
        this.currentTilt=tilt;
    }

}
