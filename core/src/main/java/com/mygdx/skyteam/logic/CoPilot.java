package com.mygdx.skyteam.logic;
/**
 * Represents the co-pilot player in the game.
 * Handles co-pilot actions such as placing dice, using the radio, and interacting with airplane components.
 * Coded by: Marija Voloder (Class structure, constructors, some function
 * definitions/implementations and variables) & Rathin (help with some functions)
 */
public class CoPilot extends Player {
    private Radio radio;

    /**
     * Initializes a co-pilot with a name, role, and assigned airplane.
     *
     * @param name      the name of the co-pilot.
     * @param airplane  the airplane associated with the co-pilot.
     */
    public CoPilot(String name, Airplane airplane) {
        super(name, "CoPilot", airplane);
        this.airplane = airplane;
        radio = new Radio(2);
    }

    /**
     * Places a dice on a specified field based on the co-pilot's input.
     * Executes corresponding actions like adjusting speed, tilt, or deploying flaps.
     *
     * @param diceValue    the value of the dice to place.
     * @param playerInput  the target component (e.g., "engine", "axis", "radio").
     * @param fieldChoice  the specific field for placement (used for components like "radio" or "flaps").
     *
     * Written by: Rathin, Marija
     */
    public void placeDice(int diceValue, String playerInput, int fieldChoice) {

        if (!canPlaceDice(diceValue, playerInput, fieldChoice)) {
            /*
             * System.out.println("Cannot place dice on " + playerInput +
             * ". The field is already occupied.");
             */
            return;
        }
        switch (playerInput.toLowerCase()) {
            case "engine":
                Field coPilotEngineField = airplane.getEngine().getCoPilotField();
                airplane.getEngine().placeCoPilotDice(diceValue);
                Field pilotEngineField = airplane.getEngine().getPilotField();
                if (pilotEngineField.isFilled()) {
                    airplane.adjustSpeed();
                }
                break;

            case "axis":
                Field coPilotAxisField = airplane.getAxis().getCoPilotAxisField();
                airplane.getAxis().placeCoPilotDice(diceValue);

                Field pilotAxisField = airplane.getAxis().getPilotAxisField();
                if (pilotAxisField.isFilled()) {
                    airplane.adjustTilt();
                }
                break;

            case "radio":
                useRadio(airplane.getEngine().getCurrentPosition(), diceValue, fieldChoice);
                break;

            case "flaps":
                airplane.deployFlaps(diceValue, fieldChoice);
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
     * Checks if the dice can be placed on a specific field.
     *
     * @param diceValue    the value of the dice to place.
     * @param playerInput  the target component (e.g., "engine", "axis", "radio").
     * @param fieldChoice  the specific field for placement (used for components like "radio").
     * @return true if the dice can be placed, false otherwise.
     * Written by: Marija
     */
    public boolean canPlaceDice(int diceValue, String playerInput, int fieldChoice) {
        boolean validPlacement = false;

        switch (playerInput.toLowerCase()) {
            case "engine":
                Field coPilotEngineField = airplane.getEngine().getCoPilotField();
                if (coPilotEngineField.isFilled()) {
                    /*
                     * System.out.println("Cannot place dice on the Engine. It's already occupied."
                     * );
                     */
                } else {
                    validPlacement = true;
                }
                break;

            case "axis":
                Field coPilotAxisField = airplane.getAxis().getCoPilotAxisField();
                if (coPilotAxisField.isFilled()) {
                    /*
                     * System.out.println("Cannot place dice on the Axis. It's already occupied.");
                     */
                } else {
                    validPlacement = true;
                }
                break;

            case "radio":
                if (getRadio().getRadioFields().get(fieldChoice).isFilled()) {
                    /*
                     * System.out.println("Cannot place dice on the Radio. It's already occupied.");
                     */
                } else {
                    validPlacement = true;
                }
                break;

            case "flaps":

                for (int i = 0; i < airplane.getFlaps().getFlapsFields().size(); i++) {
                    if (airplane.getFlaps().canPlaceFlap(diceValue, fieldChoice)) {
                        validPlacement = true;
                        break;
                    }
                }
                break;

            case "coffee":
                if (airplane.getConcentration().getCoffeeFields().get(0).isFilled()
                        && airplane.getConcentration().getCoffeeFields().get(1).isFilled()
                        && airplane.getConcentration().getCoffeeFields().get(2).isFilled()) {
                    /* System.out.println("All coffee fields are already filled."); */
                } else {
                    validPlacement = true;
                }
                break;

            default:
                /* System.out.println("Invalid input. Read the rules first, FOOL!"); */
                break;
        }

        return validPlacement;
    }

    /**
     * Uses the radio to remove the number of planes from the track based on the dice value.
     *
     * @param currentPosition the current position of the airplane.
     * @param diceValue       the dice value to use.
     * @param chosenField     the field index (0 or 1).
     * @throws IllegalArgumentException if the field choice is invalid.
     * Written by: Marija
     */
    public void useRadio(int currentPosition, int diceValue, int chosenField) {

        while (chosenField != 0 && chosenField != 1) {
            throw new IllegalArgumentException("Invalid field choice. Must be 0 or 1.");
        }

        radio.useRadio(currentPosition, diceValue, chosenField, airplane.getPlanesOnTrack());
    }

    /**
     * Checks if the co-pilot has placed dice on all required fields (engine and axis).
     *
     * @return true if the required fields have dice, false otherwise.
     * Written by: Marija
     */
    public boolean hasDicesOnRequiredFields() {
        if (airplane.getEngine().getCoPilotField().getPlacedDice() == null ||
                airplane.getAxis().getCoPilotAxisField().getPlacedDice() == null) {
            return false;
        }
        return true;
    }

    /**
     * Gets the radio object associated with the co-pilot.
     *
     * @return the radio object.
     * Written by: Marija
     */
    public Radio getRadio() {
        return radio;
    }
}
