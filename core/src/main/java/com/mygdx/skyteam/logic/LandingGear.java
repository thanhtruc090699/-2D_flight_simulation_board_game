package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class LandingGear{

    private ArrayList<Field> landingGearFields;

    public LandingGear(){
        landingGearFields = new ArrayList<>();

        landingGearFields.add(new Field("Landing Gear 1", Arrays.asList(1, 2)));
        landingGearFields.add(new Field("Landing Gear 2", Arrays.asList(3, 4)));
        landingGearFields.add(new Field("Landing Gear 3", Arrays.asList(5, 6)));
    }

    public void deployLandingGear(int pilotInput, Airplane airplane, int fieldChoice) {

        Field selectedField = landingGearFields.get(fieldChoice - 1);

        if (!selectedField.getValidDiceValues().contains(pilotInput)) {
            System.out.println("Invalid dice value for " + selectedField.getName() + "!");
            return;  
        }

        if (fieldChoice > 1 && !landingGearFields.get(fieldChoice - 2).isFilled()) {
            System.out.println("Landing gear field " + (fieldChoice - 1) + " should be deployed first.");
            return;  
        }

        selectedField.setDiceValue(pilotInput);
        airplane.getEngine().shiftBlueMarker(); 
        System.out.println(selectedField.getName() + " is now deployed.");
    }

    public ArrayList<Field> getLandingGearFields() {
        return landingGearFields;
    }
}