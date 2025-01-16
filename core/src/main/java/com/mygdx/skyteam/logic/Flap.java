package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Manages the flaps of the airplane.
 * Handles deploying flaps based on dice values and updating orange marker value and position.
 */
public class Flap {
    private ArrayList<Field> flapsFields;

    public Flap() {
        flapsFields = new ArrayList<>();
        flapsFields.add(new Field("Flap 1", Arrays.asList(1, 2), 1139, 648));
        flapsFields.add(new Field("Flap 2", Arrays.asList(2, 3), 1139, 756));
        flapsFields.add(new Field("Flap 3", Arrays.asList(4, 5), 1139, 865));
        flapsFields.add(new Field("Flap 4", Arrays.asList(5, 6), 1139, 972));

    }

    public void deployFlaps(int coPilotInput, Airplane airplane, int fieldChoice) {

        Field selectedFlap = flapsFields.get(fieldChoice - 1);

        if (!selectedFlap.getValidDiceValues().contains(coPilotInput)) {
            System.out.println("Invalid dice value for " + selectedFlap.getName() + "!");
            return;
        }

        if (fieldChoice > 1 && !flapsFields.get(fieldChoice - 2).isFilled()) { // -2 because the input is not index
                                                                               // based
            System.out.println("Flap " + (fieldChoice - 1) + " should be deployed first.");
            return;
        }

        if (selectedFlap.isFilled()) {
            System.out.println("Flap " + (fieldChoice) + " is already filled.");
            return;
        }

        selectedFlap.setDiceValue(coPilotInput);
        airplane.getEngine().shiftOrangeMarker();
        System.out.println("Flap " + fieldChoice + " is now deployed.");
    }

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

    public ArrayList<Field> getFlapsFields() {
        return flapsFields;
    }

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
