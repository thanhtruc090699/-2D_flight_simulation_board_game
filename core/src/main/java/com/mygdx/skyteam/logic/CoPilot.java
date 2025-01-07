package com.mygdx.skyteam.logic;

public class CoPilot extends Player {
    private Radio radio;

    public CoPilot(String name, Airplane airplane) {
        super(name, "CoPilot", airplane);
        this.airplane = airplane;
        radio = new Radio(2);
    }

    public void placeDice(int diceValue, String playerInput, int fieldChoice) {

        if (!canPlaceDice(diceValue, playerInput, fieldChoice)) {
            System.out.println("Cannot place dice on " + playerInput + ". The field is already occupied.");
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
                System.out.println("Invalid input. Read the rules first, FOOL!");
                break;
        }
    }

    public boolean canPlaceDice(int diceValue, String playerInput, int fieldChoice) {
        boolean validPlacement = false;

        switch (playerInput.toLowerCase()) {
            case "engine":
                Field coPilotEngineField = airplane.getEngine().getCoPilotField();
                if (coPilotEngineField.isFilled()) {
                    System.out.println("Cannot place dice on the Engine. It's already occupied.");
                } else {
                    validPlacement = true;
                }
                break;

            case "axis":
                Field coPilotAxisField = airplane.getAxis().getCoPilotAxisField();
                if (coPilotAxisField.isFilled()) {
                    System.out.println("Cannot place dice on the Axis. It's already occupied.");
                } else {
                    validPlacement = true;
                }
                break;

            case "radio":
                if (getRadio().getRadioFields().get(fieldChoice).isFilled()) {
                    System.out.println("Cannot place dice on the Radio. It's already occupied.");
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
                    System.out.println("All coffee fields are already filled.");
                } else {
                    validPlacement = true;
                }
                break;

            default:
                System.out.println("Invalid input. Read the rules first, FOOL!");
                break;
        }

        return validPlacement;
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
