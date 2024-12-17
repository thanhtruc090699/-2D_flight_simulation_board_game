package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Radio {
    private ArrayList<Field> radioFields;

    public Radio(int numFields) {
        radioFields = new ArrayList<>();

            if (numFields == 1) {
                radioFields.add(new Field("Radio Field Pilot 1", Arrays.asList(1, 2, 3, 4, 5, 6), 733, 492));

            } else if (numFields == 2) {
                radioFields.add(new Field("Radio Field Copilot 1", Arrays.asList(1, 2, 3, 4, 5, 6), 1139, 420 ));
                radioFields.add(new Field("Radio Field 2", Arrays.asList(1, 2, 3, 4, 5, 6), 1139, 492));
               
            } 
        
    }

    public void useRadio(int currentPosition, int diceValue, int chosenField, ArrayList<Integer> planesOnTrack) {

        int targetPosition = currentPosition + diceValue;

        if (targetPosition > 6) {
            targetPosition = 6;
        }

        Field selectedField = radioFields.get(chosenField);

        if (selectedField.isFilled()) {
            System.out.println("Field " + chosenField + " is already filled.");
        } else {
            selectedField.setDiceValue(diceValue);
            System.out.println("Field " + chosenField + " is now filled.");
            removePlane(targetPosition, planesOnTrack);
        }

    }

    public void removePlane(int position, ArrayList<Integer> planesOnTrack) {
        if (planesOnTrack.get(position) > 0) {
            planesOnTrack.set(position, planesOnTrack.get(position) - 1);
            System.out.println("Plane removed from field " + position); // track has 7 fields..
        } else {
            System.out.println("No planes at field " + position + " to remove.");
        }

        System.out.println("Current Track: " + planesOnTrack);
    }

    public void resetFields() {
        for (Field field : radioFields) {
            field.resetField(); // This will reset both 'isFilled' and 'placedDice'
        }
    }

    public ArrayList<Field> getRadioFields() {
        return radioFields;
    }

}
