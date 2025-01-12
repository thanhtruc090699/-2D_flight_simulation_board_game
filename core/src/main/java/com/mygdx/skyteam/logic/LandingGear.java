package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class LandingGear {

    private ArrayList<Field> landingGearFields;

    public LandingGear() {
        landingGearFields = new ArrayList<>();

        landingGearFields.add(new Field("Landing Gear 1", Arrays.asList(1, 2), 733, 648));
        landingGearFields.add(new Field("Landing Gear 2", Arrays.asList(3, 4), 733, 756));
        landingGearFields.add(new Field("Landing Gear 3", Arrays.asList(5, 6), 733, 863));
    }

    public void deployLandingGear(int pilotInput, Airplane airplane, int fieldChoice) {

        Field selectedField = landingGearFields.get(fieldChoice - 1);

        if (!selectedField.getValidDiceValues().contains(pilotInput)) {
            System.out.println("Invalid dice value for " + selectedField.getName() + "!");
            return;
        }

        selectedField.setDiceValue(pilotInput);
        airplane.getEngine().shiftBlueMarker();
        System.out.println(selectedField.getName() + " is now deployed.");
    }

    public boolean canPlaceLandingGear(int pilotInput, int fieldChoice) {
        Field selectedField = landingGearFields.get(fieldChoice - 1);
        if (!selectedField.getValidDiceValues().contains(pilotInput)) {
            return false;
        }

        if (fieldChoice > 1 && !landingGearFields.get(fieldChoice - 2).isFilled()) {
            return false;
        }
        return true;
    }

    public ArrayList<Field> getLandingGearFields() {
        return landingGearFields;
    }

    
}