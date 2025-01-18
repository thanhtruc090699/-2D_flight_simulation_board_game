package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the engine of the airplane.
 * Manages speed, position, and markers for the airplane.
 */
public class Engine {
    private int currentPosition;
    private int airportLocation;
    private int speed;
    private int blueMarker;
    private int orangeMarker;

    private Field pilotField;
    private Field coPilotField;

    private boolean isPositionMoveSuccessful;

    /**
     * Initializes the engine with default values for markers, position, and fields.
     */
    public Engine() {
        currentPosition = 0;
        airportLocation = 6;
        speed = 0;
        blueMarker = 4;
        orangeMarker = 8;
        pilotField = new Field("Pilot Engine Field", Arrays.asList(1, 2, 3, 4, 5, 6), 860, 748);
        coPilotField = new Field("CoPilot Engine Field", Arrays.asList(1, 2, 3, 4, 5, 6), 1010, 748);
    }

    /**
     * Places a dice in the pilot's engine field.
     *
     * @param diceValue the value of the dice to place.
     * @throws IllegalArgumentException if the dice value is invalid.
     */
    public void placePilotDice(int diceValue) {
        if (!pilotField.setDiceValue(diceValue)) {
            throw new IllegalArgumentException("Invalid dice value for Pilot Engine Field");
        }
    }

    /**
     * Places a dice in the co-pilot's engine field.
     *
     * @param diceValue the value of the dice to place.
     * @throws IllegalArgumentException if the dice value is invalid.
     */
    public void placeCoPilotDice(int diceValue) {
        if (!coPilotField.setDiceValue(diceValue)) { // calls the function which returns true/false
            throw new IllegalArgumentException("Invalid dice value for Co-Pilot Engine Field");
        }
    }

    /**
     * Shifts the blue marker to the next position, up to a maximum value of 7.
     */
    public void shiftBlueMarker() {
        if (blueMarker == 7) {
            blueMarker = 7;
        } else {
            blueMarker++;
        }

    }

    /**
     * Shifts the orange marker to the next position, up to a maximum value of 12.
     */
    public void shiftOrangeMarker() {
        if (orangeMarker == 12) {
            orangeMarker = 12;
        } else {
            orangeMarker++;
        }
    }

    /**
     * Adjusts the airplane's speed based on the dice values placed in the engine
     * fields.
     * Updates the airplane's position based on the adjusted speed.
     *
     * @param planeOnTrack the current state of planes on the track.
     */
    public void adjustSpeed(ArrayList<Integer> planeOnTrack) {
        speed = pilotField.getPlacedDice() + coPilotField.getPlacedDice();
        /* System.out.println("Speed adjusted to: " + speed); */
        updatePosition(planeOnTrack);
    }

    /**
     * Updates the airplane's position on the track based on its speed.
     * Ensures the airplane adheres to track rules and avoids obstacles.
     *
     * @param planesOnTrack the current state of planes on the track.
     */
    public void updatePosition(ArrayList<Integer> planesOnTrack) {
        isPositionMoveSuccessful = false;

        if (speed < blueMarker) {
            /* System.out.println("Plane does not move."); */
            isPositionMoveSuccessful = true;
            return;
        }

        if (speed == blueMarker) {
            isPositionMoveSuccessful = true;
            return;
        }
        if (speed == orangeMarker) {
            if (checkPlanesOnTrack(planesOnTrack, currentPosition)) {
                currentPosition++;
                isPositionMoveSuccessful = true;
                /* System.out.println(
                        "Speed matches orange marker. Plane moves 1 step. Current position: " + currentPosition); */
            }
            return;}
        if (currentPosition == airportLocation && speed >= blueMarker) {/* System.out.println("You lost! You overshot the airport."); */
            isPositionMoveSuccessful = false;
            return;}
        if (speed > blueMarker && speed < orangeMarker) {
            if (checkPlanesOnTrack(planesOnTrack, currentPosition)) {
                currentPosition++;
                /* System.out.println("Plane moves 1 step. Current position: " +
                 * currentPosition); */
                isPositionMoveSuccessful = true;
            } else {/* System.out.println("Obstacle on track. Plane does not move."); */
                isPositionMoveSuccessful = false;}
        } else if (speed > orangeMarker) {
            if (checkPlanesOnTrack(planesOnTrack, currentPosition) &&
                    checkPlanesOnTrack(planesOnTrack, currentPosition + 1)) {
                currentPosition += 2;
                /*
                 * System.out.println("Plane moves 2 steps. Current position: " +
                 * currentPosition);
                 */
                isPositionMoveSuccessful = true;
            }
            if (currentPosition >= airportLocation) {
                /* System.out.println("Plane overshot the airport."); */
                isPositionMoveSuccessful = false;
            }
        }

        /* System.out.println("End of updatePosition logic."); */
    }

    /**
     * Checks if the track at a specific position is clear of obstacles.
     *
     * @param planesOnTrack   the state of planes on the track.
     * @param currentPosition the position to check.
     * @return true if the track is clear, false otherwise.
     */
    public boolean checkPlanesOnTrack(ArrayList<Integer> planesOnTrack, int currentPosition) {
        if (planesOnTrack.get(currentPosition) != 0) {
            /* System.out.println("There were planes on your track. You crashed."); */
            return false;
        } else {
            return true;
        }}

    /**
     * Gets the current position of the airplane.
     *
     * @return the current position.
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Gets the current speed of the airplane.
     *
     * @return the current speed.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the pilot's engine field.
     *
     * @return the pilot's engine field.
     */
    public Field getPilotField() {
        return pilotField;
    }

    /**
     * Gets the co-pilot's engine field.
     *
     * @return the co-pilot's engine field.
     */
    public Field getCoPilotField() {
        return coPilotField;
    }

    /**
     * Gets the blue marker's position.
     *
     * @return the blue marker's position.
     */
    public int getBlueMarker() {
        return blueMarker;
    }

    /**
     * Gets the orange marker's position.
     *
     * @return the orange marker's position.
     */
    public int getOrangeMarker() {
        return orangeMarker;
    }

    /**
     * Sets the blue marker to a new value.
     *
     * @param value the new value for the blue marker.
     * @return the updated blue marker value.
     */
    public int setBlueMarker(int value) {
        return blueMarker = value;
    }

    /**
     * Sets the airplane's current position.
     *
     * @param position the new position of the airplane.
     * @return the updated position.
     */
    public int setCurrentPosition(int position) {
        return currentPosition = position;
    }

    /**
     * Sets the airplane's speed.
     *
     * @param newSpeed the new speed of the airplane.
     * @return the updated speed.
     */
    public int setSpeed(int newSpeed) {
        return speed = newSpeed;
    }

    /**
     * Resets both the pilot and co-pilot fields.
     */
    public void resetFields() {
        pilotField.resetField();
        coPilotField.resetField();
    }

    /* Checks if the last move was successful.
     *
     * @return true if the move was successful, false otherwise.
     */
    public boolean isPositionMoveSuccessful() {
        return isPositionMoveSuccessful;
    }

    /**
     * Sets if the last position move was successful.
     *
     * @param x true if successful, false otherwise.
     * @return the updated move success status.
     */
    public boolean setPositionMoveSuccessful(boolean x) {
        return isPositionMoveSuccessful = x;
    }
}
