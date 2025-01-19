package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Manages the airplane's landing gear system.
 * Handles deploying landing gears based on pilot input and updating related game mechanics.
 */
public class LandingGear {

    private ArrayList<Field> landingGearFields;

    /**
     * Initializes the landing gear fields with default configurations.
     *
     * Written by: Rathin
     */
    public LandingGear() {
        landingGearFields = new ArrayList<>();

        landingGearFields.add(new Field("Landing Gear 1", Arrays.asList(1, 2), 733, 648));
        landingGearFields.add(new Field("Landing Gear 2", Arrays.asList(3, 4), 733, 756));
        landingGearFields.add(new Field("Landing Gear 3", Arrays.asList(5, 6), 733, 863));
    }

    /**
     * Deploys a landing gear based on the pilot's input and the selected field.
     * Updates the airplane's engine blue marker when the landing gear is deployed.
     *
     * @param pilotInput  the dice value provided by the pilot.
     * @param airplane    the airplane to update (e.g., shift markers).
     * @param fieldChoice the landing gear field to deploy (1-based index).
     *
     * Written by: Rathin
     */
    public void deployLandingGear(int pilotInput, Airplane airplane, int fieldChoice) {

        Field selectedField = landingGearFields.get(fieldChoice - 1);

        if (!selectedField.getValidDiceValues().contains(pilotInput)) {
            /* System.out.println("Invalid dice value for " + selectedField.getName() + "!"); */
            return;
        }

        selectedField.setDiceValue(pilotInput);
        airplane.getEngine().shiftBlueMarker();
        /* System.out.println(selectedField.getName() + " is now deployed."); */
    }

    /**
     * Checks if a landing gear can be deployed based on the pilot's input and selected field.
     *
     * @param pilotInput  the dice value to check.
     * @param fieldChoice the landing gear field to check (1-based index).
     * @return true if the landing gear can be deployed, false otherwise.
     * Written by: Marija
     */
    public boolean canPlaceLandingGear(int pilotInput, int fieldChoice) {
        Field selectedField = landingGearFields.get(fieldChoice - 1);

        if (!selectedField.getValidDiceValues().contains(pilotInput)) {
            // The dice value is not valid for this field.
            return false;
        }

        if (selectedField.isFilled()) {
            // The field is already filled; cannot place another landing gear.
            return false;
        }

        return true;
    }

    /**
     * Gets the list of landing gear fields.
     *
     * @return an ArrayList of landing gear fields.
     * Written by: Marija
     */
    public ArrayList<Field> getLandingGearFields() {
        return landingGearFields;
    }

}
