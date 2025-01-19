package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Manages the flaps of the airplane.
 * Handles deploying flaps based on dice values and updating related game mechanics.
 */
public class Flap {
    private ArrayList<Field> flapsFields;

    /**
     * Initializes the flap fields with default configurations.
     */
    public Flap() {
        flapsFields = new ArrayList<>();
        flapsFields.add(new Field("Flap 1", Arrays.asList(1, 2), 1139, 648));
        flapsFields.add(new Field("Flap 2", Arrays.asList(2, 3), 1139, 756));
        flapsFields.add(new Field("Flap 3", Arrays.asList(4, 5), 1139, 865));
        flapsFields.add(new Field("Flap 4", Arrays.asList(5, 6), 1139, 972));

    }

    /**
     * Deploys a flap based on the co-pilot's dice input and selected field.
     * Ensures the flap is valid and previous flaps are deployed in order.
     *
     * @param coPilotInput the dice value provided by the co-pilot.
     * @param airplane     the airplane to update (e.g., shifting markers).
     * @param fieldChoice  the flap field to deploy (1-based index).
     *
     * Written by: Rathin
     */
    public void deployFlaps(int coPilotInput, Airplane airplane, int fieldChoice) {

        Field selectedFlap = flapsFields.get(fieldChoice - 1);

        if (!selectedFlap.getValidDiceValues().contains(coPilotInput)) {
            /* System.out.println("Invalid dice value for " + selectedFlap.getName() + "!"); */
            return;
        }

        if (fieldChoice > 1 && !flapsFields.get(fieldChoice - 2).isFilled()) { // -2 because the input is not index based

            /* System.out.println("Flap " + (fieldChoice - 1) + " should be deployed first."); */
            return;
        }

        if (selectedFlap.isFilled()) {
            /* System.out.println("Flap " + (fieldChoice) + " is already filled."); */
            return;
        }

        selectedFlap.setDiceValue(coPilotInput);
        airplane.getEngine().shiftOrangeMarker();
        /* System.out.println("Flap " + fieldChoice + " is now deployed."); */
    }

    /**
     * Checks if a flap can be deployed based on the co-pilot's dice input and selected field.
     *
     * @param coPilotInput the dice value provided by the co-pilot.
     * @param fieldChoice  the flap field to check (1-based index).
     * @return true if the flap can be deployed, false otherwise.
     */
    public boolean canPlaceFlap(int coPilotInput, int fieldChoice) {
        Field selectedFlap = flapsFields.get(fieldChoice - 1);
        if (!selectedFlap.getValidDiceValues().contains(coPilotInput)) {
            return false;
        }

        if (fieldChoice > 1 && !flapsFields.get(fieldChoice - 2).isFilled()) {
            return false;
        }
        return true;
    }

    /**
     * Gets the list of flap fields.
     *
     * @return the list of flap fields.
     */
    public ArrayList<Field> getFlapsFields() {
        return flapsFields;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Checks if a flap can be placed for a specific dice value and field.
     *
     * @param diceValue   the dice value to check.
     * @param fieldChoice the flap field to check (1-based index).
     * @return true if the flap can be placed, false otherwise.
     *
     * Written by: Rathin
     */
    public boolean canPlaceFlapsForDice(int diceValue, int fieldChoice) {
        if ((diceValue == 1 || diceValue == 2) && fieldChoice - 1 == 0) {
            if (flapsFields.get(0).isFilled()) {
                return false;
            }
        }

        if ((diceValue == 2 || diceValue == 3) && fieldChoice - 1 == 1) {
            if (!flapsFields.get(0).isFilled()) {
                return false;
            }
            if (flapsFields.get(0).isFilled() && flapsFields.get(1).isFilled()) {
                return false;
            }
        }

        if ((diceValue == 4 || diceValue == 5) && fieldChoice - 1 == 2) {
            if (!flapsFields.get(0).isFilled() || !flapsFields.get(1).isFilled()) {
                return false;
            }
            if (flapsFields.get(0).isFilled() && flapsFields.get(1).isFilled() && flapsFields.get(2).isFilled()) {
                return false;
            }
        }

        if ((diceValue == 5 || diceValue == 6) && fieldChoice - 1 == 3) {
            if (!flapsFields.get(0).isFilled() || !flapsFields.get(1).isFilled() || !flapsFields.get(2).isFilled()) {
                return false;
            }
            if (flapsFields.get(0).isFilled() && flapsFields.get(1).isFilled() && flapsFields.get(2).isFilled()
                    && flapsFields.get(3).isFilled()) {
                return false;
            }
        }

        return true;
    }

}
