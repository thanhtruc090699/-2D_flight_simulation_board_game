package com.mygdx.skyteam.logic;

public class CoPilot extends Player {
    private Radio radio;

    public CoPilot(String name, Airplane airplane) {
        super(name, "CoPilot", airplane);
        this.airplane = airplane;
        radio = new Radio(2);
    }

    public void placeDice(int diceValue, String playerInput, int fieldChoice) {
        boolean validPlacement = false;
        while (!validPlacement) {
            switch (playerInput.toLowerCase()) {
                case "engine":
                    Field coPilotEngineField = airplane.getEngine().getCoPilotField();
                    if (coPilotEngineField.isFilled()) {
                        System.out.println("Cannot place dice on the Engine. It's already occupied.");
                    } else {
                        airplane.getEngine().placeCoPilotDice(diceValue);
                        validPlacement = true;
                    }
                    Field pilotEngineField = airplane.getEngine().getPilotField();

                    if (pilotEngineField.isFilled()) {
                        airplane.adjustSpeed();
                    }
                    break;

                case "axis":
                    Field coPilotAxisField = airplane.getAxis().getCoPilotAxisField();

                    if (coPilotAxisField.isFilled()) {
                        System.out.println("Cannot place dice on the Axis. It's already occupied.");
                    } else {
                        airplane.getAxis().placeCoPilotDice(diceValue);
                        validPlacement = true;
                    }

                    Field pilotAxisField = airplane.getAxis().getPilotAxisField();
                    if (pilotAxisField.isFilled()) {
                        airplane.adjustTilt();
                    }
                    break;

                case "radio":
                    int chosenField = 0; // or 1, hardcoded now just for testing, i will implement a function in libgdx
                                         // to determine the user input/click
                    useRadio(airplane.getEngine().getCurrentPosition(), diceValue, chosenField);
                    validPlacement = true;
                    break;

                case "flaps":
                    boolean allFlapsOccupied = true;

                    for (Field flapField : airplane.getFlaps().getFlapsFields()) {
                        if (!flapField.isFilled()) {
                            allFlapsOccupied = false;
                            break;
                        }
                    }

                    if (allFlapsOccupied) {
                        System.out.println("All flap fields are already occupied.");
                    } else {
                        airplane.deployFlaps(diceValue, fieldChoice);
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

                    playerInput = "engine"; // hardcoded for testing, later it will be based on user clicks
                    break;
            }
        }
    }

    public void useRadio(int currentPosition, int diceValue, int chosenField) {

        while (chosenField != 0 && chosenField != 1) {
            throw new IllegalArgumentException("Invalid field choice. Must be 0 or 1.");
        }

        radio.useRadio(currentPosition, diceValue, chosenField, airplane.getPlanesOnTrack());
    }

    public boolean hasDicesOnRequiredFields() {
        return airplane.getEngine().getCoPilotField().getPlacedDice() != null
                && airplane.getAxis().getCoPilotAxisField().getPlacedDice() != null;
    }

    public Radio getRadio() {
        return radio;
    }
}
