package com.mygdx.skyteam.logic;

import java.util.ArrayList;

public class Pilot extends Player {
    private Radio radio;

    public Pilot(String name, Airplane airplane) {
        super(name, "Pilot", airplane);
        radio = new Radio(1);
    }

    public void placeDice(int diceValue, String playerInput, int fieldChoice) {

        if (!canPlaceDice(diceValue, playerInput, fieldChoice)) {
            System.out.println("Cannot place dice on " + playerInput + ". The field is already occupied.");
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
                System.out.println("Invalid input. Read the rules first, FOOL!");
                break;
        }
    }

    // checks if dice can be placed without modifying the game state
    public boolean canPlaceDice(int diceValue, String playerInput, int fieldChoice) {
        boolean validPlacement = false;

        switch (playerInput.toLowerCase()) {
            case "engine":
                Field pilotEngineField = airplane.getEngine().getPilotField();

                if (pilotEngineField.isFilled()) {
                    System.out.println("Cannot place dice on the Engine. It's already occupied.");
                    validPlacement = false;
                } else {
                    System.out.println("hooray you can place it");
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
                    System.out.println("Cannot place dice on the Axis. It's already occupied.");
                    validPlacement = false;
                } else {
                    System.out.println("hooray you can place it");
                    validPlacement = true;
                }

                Field coPilotField = airplane.getAxis().getCoPilotAxisField();
                if (coPilotField.isFilled()) {
                    validPlacement = true;
                }
                break;

            case "radio":
                if (getRadio().getRadioFields().get(0).isFilled()) {
                    System.out.println("Cannot place dice on the Radio. It's already occupied.");
                    validPlacement = false;
                } else {
                    validPlacement = true;
                }
                break;

            case "landing gears":
                ArrayList<Field> gearFields = airplane.getLandingGears().getLandingGearFields();
                if (gearFields.get(0).isFilled() && gearFields.get(1).isFilled() && gearFields.get(2).isFilled()) {
                    System.out.println("All landing gear fields are already occupied.");
                    validPlacement = false;
                } else {
                    validPlacement = true;
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
                    System.out.println("All coffee fields are already filled.");
                    validPlacement = false;
                } else {
                    validPlacement = true;
                }
                break;

            default:
                System.out.println("Invalid input. Read the rules first, FOOL!");
                validPlacement = false;
                break;
        }

        return validPlacement;
    }

    public boolean hasDicesOnRequiredFields() {
        return airplane.getEngine().getPilotField().getPlacedDice() != null
                && airplane.getAxis().getPilotAxisField().getPlacedDice() != null;
    }

    public Radio getRadio() {
        return radio;
    }
}
