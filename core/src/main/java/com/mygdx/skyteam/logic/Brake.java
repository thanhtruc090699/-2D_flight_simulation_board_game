package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Brake {
    private int redMarker;
    private ArrayList<Field> brakeFields;

    public Brake(){
        redMarker = 0;
        brakeFields = new ArrayList<>();

        brakeFields.add(new Field("Brake Field 1", Arrays.asList(2), 865, 893));
        brakeFields.add(new Field("Brake Field 2", Arrays.asList(4), 948, 893));
        brakeFields.add(new Field("Brake Field 3", Arrays.asList(6), 1015, 893));    	    
    }

    public void deployBrakes(int pilotInput){
        if (pilotInput == 2) {
            if (!brakeFields.get(0).isFilled()) {
                brakeFields.get(0).setDiceValue(pilotInput);
                redMarker = pilotInput;
                System.out.println("Brake field 1 is now deployed.");
            } else {
                System.out.println("Brake field 1 is already occupied.");
            }
        } else if (pilotInput == 4) {
            if (!brakeFields.get(0).isFilled()) {
                System.out.println("Brake field 1 must be deployed first before deploying brake field 2.");
            } else if (!brakeFields.get(1).isFilled()) {
                brakeFields.get(1).setDiceValue(pilotInput);
                redMarker = pilotInput;
                System.out.println("Brake field 2 is now deployed.");
            } else {
                System.out.println("Brake field 2 is already occupied.");
            }
        } else if (pilotInput == 6) {
            if (!brakeFields.get(0).isFilled() || !brakeFields.get(1).isFilled()) {
                System.out.println("Both Brake field 1 and Brake field 2 must be deployed first before deploying brake field 3.");
            } else if (!brakeFields.get(2).isFilled()) {
                brakeFields.get(2).setDiceValue(pilotInput);
                redMarker = pilotInput;
                System.out.println("Brake field 3 is now deployed.");
            } else {
                System.out.println("Brake field 3 is already occupied.");
            }
        } else {
            System.out.println("Invalid input for brake deployment. Please choose from 2, 4, or 6.");
        }
    }
    
    public void setRedMarker(int redMarker){
        if (redMarker > 1 && redMarker < 7) {
            this.redMarker = redMarker;
        } else {
            throw new IllegalArgumentException("Not a valid input");
        }
    }

    public ArrayList<Field> getBrakeFields() {
        return brakeFields;
    }

    public int getRedMarker(){
        return redMarker;
    }
}
