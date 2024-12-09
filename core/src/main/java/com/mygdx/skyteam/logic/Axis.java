package com.mygdx.skyteam.logic;

import java.util.Arrays;

public class Axis {
    private int currentTilt;
    private Field pilotField; 
    private Field coPilotField;

    public Axis(){
        currentTilt = 3;
        this.pilotField = new Field("Pilot Axis Field", Arrays.asList(1, 2, 3, 4, 5, 6));
        this.coPilotField = new Field("Co-Pilot Axis Field", Arrays.asList(1, 2, 3, 4, 5, 6));
    }

    public void adjustTilt(){

        Integer pilotDice = pilotField.getPlacedDice();
        Integer coPilotDice = coPilotField.getPlacedDice();

        if(pilotDice > coPilotDice){
            currentTilt --;
        } else if (pilotDice < coPilotDice) {
            currentTilt ++;
        }

        if (currentTilt < 1 || currentTilt > 5) {
            currentTilt = -1;
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

    public int getCurrentTilt(){
        return currentTilt;
    }

    public void resetFields() {
        pilotField.resetField();  
        coPilotField.resetField();   
    }

}
