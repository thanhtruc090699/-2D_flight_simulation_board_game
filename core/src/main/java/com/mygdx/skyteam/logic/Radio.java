package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the radio system used by the Pilot or CoPilot to interact with the game.
 * Manages radio fields, allows removal of planes from the track, and resets fields as needed.
 */
public class Radio {
    private ArrayList<Field> radioFields;

    /**
     * Initializes the radio with the specified number of fields.
     *
     * @param numFields the number of fields to create (1 for Pilot, 2 for CoPilot).
     */
    public Radio(int numFields) {
        radioFields = new ArrayList<>();

            if (numFields == 1) {
                radioFields.add(new Field("Radio Field Pilot 1", Arrays.asList(1, 2, 3, 4, 5, 6), 733, 494));

            } else if (numFields == 2) {
                radioFields.add(new Field("Radio Field Copilot 1", Arrays.asList(1, 2, 3, 4, 5, 6), 1139, 420 ));
                radioFields.add(new Field("Radio Field 2", Arrays.asList(1, 2, 3, 4, 5, 6), 1139, 492));

            }

    }

    /**
     * Uses the radio to interact with the track by placing a dice and removing a plane.
     *
     * @param currentPosition the current position on the track.
     * @param diceValue       the dice value being used.
     * @param chosenField     the index of the radio field being used.
     * @param planesOnTrack   the list representing planes on the track.
     */
    public void useRadio(int currentPosition, int diceValue, int chosenField, ArrayList<Integer> planesOnTrack) {

        int targetPosition = currentPosition + diceValue - 1;

        // Ensure the target position doesn't exceed the track length

        if (targetPosition > 6) {
            targetPosition = 6;
        }

        Field selectedField = radioFields.get(chosenField);

        if (selectedField.isFilled()) {
            /* System.out.println("Field " + chosenField + " is already filled."); */
        } else {
            selectedField.setDiceValue(diceValue);
            /* System.out.println("Field " + chosenField + " is now filled."); */
            removePlane(targetPosition, planesOnTrack);
        }

    }

    /**
     * Removes a plane from the specified position on the track.
     *
     * @param position       the track position from which to remove a plane.
     * @param planesOnTrack  the list representing planes on the track.
     */
    public void removePlane(int position, ArrayList<Integer> planesOnTrack) {
        if (planesOnTrack.get(position) > 0) {
            planesOnTrack.set(position, planesOnTrack.get(position) - 1);
            /* System.out.println("Plane removed from field " + position);  */
        } else {
            /* System.out.println("No planes at field " + position + " to remove."); */
        }

        /* System.out.println("Current Track: " + planesOnTrack); */
    }

    /**
     * Resets all radio fields, clearing their values and states.
     */
    public void resetFields() {
        for (Field field : radioFields) {
            field.resetField(); // This will reset both 'isFilled' and 'placedDice'
        }
    }

    /**
     * Gets the list of radio fields.
     *
     * @return an ArrayList of radio fields.
     */
    public ArrayList<Field> getRadioFields() {
        return radioFields;
    }

}
