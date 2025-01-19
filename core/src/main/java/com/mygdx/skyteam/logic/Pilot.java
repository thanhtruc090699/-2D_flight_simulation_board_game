package com.mygdx.skyteam.logic;

import java.util.ArrayList;
/**
 * Represents the pilot player in the game.
 * Handles the pilot's actions, such as placing dice, using the radio, and interacting with airplane components.
 */
public class Pilot extends Player {
    private Radio radio;

    /**
     * Initializes a pilot with a name and assigned airplane.
     *
     * @param name      the name of the pilot.
     * @param airplane  the airplane associated with the pilot.
     */
    public Pilot(String name, Airplane airplane) {
        super(name, "Pilot", airplane);
        radio = new Radio(1);
    }

    /**
     * Places a dice on a specified field based on the pilot's input.
     * Executes corresponding actions, such as adjusting speed or deploying landing gears.
     *
     * @param diceValue   the value of the dice to place.
     * @param playerInput the target component (e.g., "engine", "axis", "radio").
     * @param fieldChoice the specific field for placement (used for multi-field components).
     *
     * Written by: Rathin
     */
    public void placeDice(int diceValue, String playerInput, int fieldChoice) {

        if (!canPlaceDice(diceValue, playerInput, fieldChoice)) {
            /* System.out.println("Cannot place dice on " + playerInput + ". The field is already occupied."); */
            return;
        }

        switch (playerInput.toLowerCase()) {
            case "engine":
                Field pilotEngineField = airplane.getEngine().getPilotField();
                airplane.getEngine().placePilotDice(diceValue);

                Field coPilotEngineField = airplane.getEngine().getCoPilotField();
                if (coPilotEngineField.isFilled()) {
                    airplane.adjustSpeed();
                }
                break;

            case "axis":
                Field pilotField = airplane.getAxis().getPilotAxisField();
                airplane.getAxis().placePilotDice(diceValue);
                Field coPilotField = airplane.getAxis().getCoPilotAxisField();
                if (coPilotField.isFilled()) {
                    airplane.adjustTilt();
                }
                break;

            case "radio":
                radio.useRadio(airplane.getEngine().getCurrentPosition(), diceValue, 0, airplane.getPlanesOnTrack());

                break;

            case "landing gears":
                airplane.deployLandingGear(diceValue, fieldChoice);
                break;

            case "brake":
                airplane.deployBrakes(diceValue);
                break;

            case "coffee":
                coffeeTokens.setQuantity(coffeeTokens.getQuantity() + 1);
                airplane.fillCoffeeFields(diceValue);
                break;

            default:
                /* System.out.println("Invalid input. Read the rules first, FOOL!"); */
                break;
        }
    }

    /**
     * Checks if the dice can be placed on a specific field without modifying the game state.
     *
     * @param diceValue   the value of the dice to place.
     * @param playerInput the target component (e.g., "engine", "axis", "radio").
     * @param fieldChoice the specific field to check (used for multi-field components).
     * @return true if the dice can be placed, false otherwise.
     */
    public boolean canPlaceDice(int diceValue, String playerInput, int fieldChoice) {
        boolean validPlacement = false;

        switch (playerInput.toLowerCase()) {
            case "engine":
                Field pilotEngineField = airplane.getEngine().getPilotField();

                if (pilotEngineField.isFilled()) {
                    /* System.out.println("Cannot place dice on the Engine. It's already occupied."); */
                    validPlacement = false;
                } else {
                    validPlacement = true;
                }

                Field coPilotEngineField = airplane.getEngine().getCoPilotField();
                if (coPilotEngineField.isFilled()) {
                    validPlacement = true;
                }
                break;

            case "axis":
                Field pilotField = airplane.getAxis().getPilotAxisField();
                if (pilotField.isFilled()) {
                    /* System.out.println("Cannot place dice on the Axis. It's already occupied."); */
                    validPlacement = false;
                } else {
                    validPlacement = true;
                }

                Field coPilotField = airplane.getAxis().getCoPilotAxisField();
                if (coPilotField.isFilled()) {
                    validPlacement = true;
                }
                break;

            case "radio":
                if (getRadio().getRadioFields().get(0).isFilled()) {
                    /* System.out.println("Cannot place dice on the Radio. It's already occupied."); */
                    validPlacement = false;
                } else {
                    validPlacement = true;
                }
                break;

            case "landing gears":
                ArrayList<Field> gearFields = airplane.getLandingGears().getLandingGearFields();
                for (int i = 0; i < gearFields.size(); i++) {
                    if (airplane.getLandingGears().canPlaceLandingGear(diceValue, fieldChoice)) {
                        validPlacement = true;
                        break;
                    }
                }
                break;

            case "brake":
                if (airplane.getBrakes().canPlaceBrakes(diceValue)) {
                    validPlacement = true;
                } else {
                    validPlacement = false;
                }
                break;

            case "coffee":
                if (airplane.getConcentration().getCoffeeFields().get(0).isFilled()
                        && airplane.getConcentration().getCoffeeFields().get(1).isFilled()
                        && airplane.getConcentration().getCoffeeFields().get(2).isFilled()) {
                    /* System.out.println("All coffee fields are already filled."); */
                    validPlacement = false;
                } else {
                    validPlacement = true;
                }
                break;

            default:
                /* System.out.println("Invalid input. Read the rules first, FOOL!"); */
                validPlacement = false;
                break;
        }

        return validPlacement;
    }

    /**
     * Checks if the pilot has placed dice on all required fields.
     *
     * @return true if all required fields have dice, false otherwise.
     */
    public boolean hasDicesOnRequiredFields() {
        if (airplane.getEngine().getPilotField().getPlacedDice() == null ||
            airplane.getAxis().getPilotAxisField().getPlacedDice() == null) {
            return false;
        }
        return true;
    }


    /**
     * Gets the radio object associated with the pilot.
     *
     * @return the radio object.
     */
    public Radio getRadio() {
        return radio;
    }

}
