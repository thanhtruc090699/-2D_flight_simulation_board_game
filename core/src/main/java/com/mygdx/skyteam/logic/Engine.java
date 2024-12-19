package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
    private int currentPosition;
    private int airportLocation;
    private int speed;
    private int blueMarker;
    private int orangeMarker;

    private Field pilotField;
    private Field coPilotField;

    private boolean isPositionMoveSuccessful;

    public Engine() {
        currentPosition = 0;
        airportLocation = 6;
        speed = 0;
        blueMarker = 4;
        orangeMarker = 8;
        pilotField = new Field("Pilot Engine Field", Arrays.asList(1, 2, 3, 4, 5, 6), 860, 748);
        coPilotField = new Field("CoPilot Engine Field", Arrays.asList(1, 2, 3, 4, 5, 6), 1012, 748);
    }

    public void placePilotDice(int diceValue) {
        if (!pilotField.setDiceValue(diceValue)) {
            throw new IllegalArgumentException("Invalid dice value for Pilot Engine Field");
        }
    }

    public void placeCoPilotDice(int diceValue) {
        if (!coPilotField.setDiceValue(diceValue)) { // calls the function which returns true/false
            throw new IllegalArgumentException("Invalid dice value for Co-Pilot Engine Field");
        }
    }

    public void shiftBlueMarker() {
        if (blueMarker == 7) {
            blueMarker = 7;
        } else {
            blueMarker++;
        }

    }

    public void shiftOrangeMarker() {
        if (orangeMarker == 12) {
            orangeMarker = 12;
        } else {
            orangeMarker++;
        }
    }

    public void adjustSpeed(ArrayList<Integer> planeOnTrack) {
        speed = pilotField.getPlacedDice() + coPilotField.getPlacedDice();
        System.out.println("Speed adjusted to: " + speed);
        updatePosition(planeOnTrack);
    }

    public void updatePosition(ArrayList<Integer> planesOnTrack) {
        isPositionMoveSuccessful = false;

        if (speed < blueMarker) {
            System.out.println("Plane does not move.");
            isPositionMoveSuccessful = true;
        }
        if (currentPosition == airportLocation && speed >= blueMarker) {
            System.out.println("You lost! You overshot the airport.");
            isPositionMoveSuccessful = false;
            return;
        }
        if (speed > blueMarker && speed <= orangeMarker) {
            if (checkPlanesOnTrack(planesOnTrack, currentPosition + 1)) {
                currentPosition++;
                System.out.println("Plane moves 1 step. Current position: " + currentPosition);
                isPositionMoveSuccessful = true;

            }

        } else if (speed > orangeMarker) {
            if (checkPlanesOnTrack(planesOnTrack, currentPosition + 1) &&
                    checkPlanesOnTrack(planesOnTrack, currentPosition + 2)) {
                currentPosition += 2;
                System.out.println("Plane moves 2 steps. Current position: " + currentPosition);
                isPositionMoveSuccessful = true;
            }
            if (currentPosition >= airportLocation) {
                System.out.println("Plane overshot the airport.");
                isPositionMoveSuccessful = false;
                return; // Stop further processing
            }
        }

    }

    public boolean checkPlanesOnTrack(ArrayList<Integer> planesOnTrack, int currentPosition) {
        if (planesOnTrack.get(currentPosition) != 0) {
            System.out.println("There were planes on your track. You crashed.");
            return false;
        } else {
            return true;
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getSpeed() {
        return speed;
    }

    public Field getPilotField() {
        return pilotField;
    }

    public Field getCoPilotField() {
        return coPilotField;
    }

    public int getBlueMarker() {
        return blueMarker;
    }

    public int getOrangeMarker() {
        return orangeMarker;
    }

    public void resetFields() {
        pilotField.resetField();
        coPilotField.resetField();
    }

    public boolean isPositionMoveSuccessful() {
        return isPositionMoveSuccessful;
    }

}
