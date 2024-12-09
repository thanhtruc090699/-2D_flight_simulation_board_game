package com.mygdx.skyteam.logic;

import java.util.ArrayList;

public class Pilot extends Player {
    private Radio radio;

    public Pilot(String name, Airplane airplane) {
        super(name, "Pilot", airplane);
        radio = new Radio(1);
    }

    public void placeDice(int diceValue, String playerInput, int fieldChoice) {
        boolean validPlacement = false;
        // to track if they both put dice on their field so we can call the mutual function

        while (!validPlacement) {
            switch (playerInput.toLowerCase()) {
                case "engine":
                    Field pilotEngineField = airplane.getEngine().getPilotField();

                    if (pilotEngineField.isFilled()) {
                        System.out.println("Cannot place dice on the Engine. It's already occupied.");
                    } else {
                        airplane.getEngine().placePilotDice(diceValue);
                        validPlacement = true;
                    }

                    Field coPilotEngineField = airplane.getEngine().getCoPilotField();
                    if (coPilotEngineField.isFilled()) {
                        airplane.adjustSpeed();
                    }
                    break;

                case "axis":
                    Field pilotField = airplane.getAxis().getPilotAxisField();

                    if (pilotField.isFilled()) {
                        System.out.println("Cannot place dice on the Axis. It's already occupied.");
                    } else {
                        airplane.getAxis().placePilotDice(diceValue);
                        validPlacement = true;
                    }

                    Field coPilotField = airplane.getAxis().getCoPilotAxisField();

                    if (coPilotField.isFilled()) {
                        airplane.adjustTilt();
                    }

                    break;

                case "radio":
                    radio.useRadio(airplane.getEngine().getCurrentPosition(), diceValue, 0,
                            airplane.getPlanesOnTrack());
                    validPlacement = true;
                    break;

                case "landing gears":
                    ArrayList<Field> gearFields = airplane.getLandingGears().getLandingGearFields();

                    if (gearFields.get(0).isFilled() && gearFields.get(1).isFilled() && gearFields.get(2).isFilled()) {
                        System.out.println("All landing gear fields are already occupied.");
                    } else {
                        airplane.deployLandingGear(diceValue, fieldChoice);
                        validPlacement = true;
                    }
                    break;

                case "brake":
                    ArrayList<Field> brakeStatus = airplane.getBrakes().getBrakeFields();

                    if (brakeStatus.get(0).isFilled() && brakeStatus.get(1).isFilled()
                            && brakeStatus.get(2).isFilled()) {
                        System.out.println("All brake fields are already occupied.");
                    } else {
                        airplane.deployBrakes(diceValue);
                        validPlacement = true;
                    }
                    break;

                case "coffee":
                    if (airplane.getConcentration().getCoffeeFields().get(0).isFilled()
                            && airplane.getConcentration().getCoffeeFields().get(1).isFilled()
                            && airplane.getConcentration().getCoffeeFields().get(2).isFilled()) {
                        System.out.println("All coffee fields are already filled.");
                    } else {
                        coffeeTokens.setQuantity(coffeeTokens.getQuantity() + 1);
                        airplane.fillCoffeeFields(diceValue);
                        validPlacement = true;
                    }
                    break;

                default:
                    System.out.println("Invalid input. Read the rules first, FOOL!");
                    playerInput = "engine"; // hardcoded value for testing
                    break;
            }
        }
    }

    public boolean hasDicesOnRequiredFields() {
        return airplane.getEngine().getPilotField().getPlacedDice() != null
                && airplane.getAxis().getPilotAxisField().getPlacedDice() != null;
    }

    public Radio getRadio() {
        return radio;
    }
}
