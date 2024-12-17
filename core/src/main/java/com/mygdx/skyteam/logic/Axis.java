package com.mygdx.skyteam.logic;

import java.util.Arrays;

public class Axis {
    private int currentTilt;
    private Field pilotField;
    private Field coPilotField;

    public Axis() {
        currentTilt = 3;
        this.pilotField = new Field("Pilot Axis Field", Arrays.asList(1, 2, 3, 4, 5, 6), 812, 500);
        this.coPilotField = new Field("Co-Pilot Axis Field", Arrays.asList(1, 2, 3, 4, 5, 6), 1064, 506);
    }

    public void adjustTilt() {

        Integer pilotDice = pilotField.getPlacedDice();
        Integer coPilotDice = coPilotField.getPlacedDice();

        int difference = Math.abs(pilotDice - coPilotDice);

        if (pilotDice > coPilotDice) {
            currentTilt -= difference;
        } else if (coPilotDice > pilotDice) {
            currentTilt += difference;
        }

        if (currentTilt < 1) {
            currentTilt = 0;
        } else if (currentTilt > 5) {
            currentTilt = 6;
        }

        if (currentTilt == 0 || currentTilt == 6) {
            System.out.println("Plane tilted 90 degrees. GAME OVER");
        } else {
            System.out.println("Axis adjusted. Current tilt: " + currentTilt);
        }
    }

    public void placePilotDice(int diceValue) {
        if (!pilotField.setDiceValue(diceValue)) {
            throw new IllegalArgumentException("Not a valid dice input for Pilot Axis Field");
        }
    }

    public void placeCoPilotDice(int diceValue) {
        if (!coPilotField.setDiceValue(diceValue)) {
            throw new IllegalArgumentException("Not a valid dice input for Co-Pilot Axis Field");
        }
    }

    public Field getPilotAxisField() {
        return pilotField;
    }

    public Field getCoPilotAxisField() {
        return coPilotField;
    }

    public int getCurrentTilt() {
        return currentTilt;
    }

    public void resetFields() {
        pilotField.resetField();
        coPilotField.resetField();
    }

    public float getRotationAngle() {
        switch (currentTilt) {
            case 0:
                return 90f;
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
                return -90f;
            default:
                return 0f;
        }
    }

}
