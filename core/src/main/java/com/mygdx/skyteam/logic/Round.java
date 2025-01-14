package com.mygdx.skyteam.logic;

public class Round {
    private int turnsLeft;
    private Pilot pilot;
    private CoPilot coPilot;
    private int currentPlayerIndex;
    private Airplane airplane;
    private GameLogic game;

    private String currentPlayerInput;
    private int currentFieldChoice;
    private int currentDiceIndex;

    public Round(Pilot pilot, CoPilot coPilot, Airplane airplane, GameLogic game) {
        turnsLeft = 4;
        this.pilot = pilot;
        this.coPilot = coPilot;
        currentPlayerIndex = 0;
        this.airplane = airplane;
        this.game = game;
    }

    public void playRound() {
        boolean turnCompleted = false;
        while (turnsLeft > 0) {

            if (currentDiceIndex != -1 && currentPlayerInput != null && currentFieldChoice != -1) {
                playTurn(currentDiceIndex, currentPlayerInput, currentFieldChoice);

                if (currentPlayerIndex == 1) {
                    turnsLeft--;
                }
                switchPlayer();
                turnCompleted = true;
            }
            if (turnCompleted) {
                break;
            }
        }
        if (turnsLeft == 0) {
            game.nextRound();
        }
    }

    public void playTurn(int diceIndex, String playerInput, int fieldChoice) {
        // Pilots turn
        if (currentPlayerIndex == 0) {
            System.out.println("It's Pilot's turn.");
            pilot.displayUnassignedDice();

            boolean tokenUsed = false;

            String pilotReroll = "no"; // harcoded value
            if (pilot.hasRerollToken()) {
                System.out.println("You have a reroll token. Do you want to use it? (yes/no)");

                if (pilotReroll.equals("yes")) {
                    pilot.useRerollToken();
                    pilot.displayUnassignedDice();
                    tokenUsed = true;
                } else {
                    System.out.println("Pilot decided not to use the reroll token.");
                }

                String coPilotReroll = "no"; // hardcoded value
                System.out.println("CoPilot, do you want to reroll too? (yes/no)");

                if (coPilotReroll.equals("yes")) {
                    coPilot.useRerollToken();
                    pilot.displayUnassignedDice();
                    tokenUsed = true;

                } else {
                    System.out.println("CoPilot decided not to use the reroll token.");
                }
            }

            if (tokenUsed) {
                airplane.getAltitude().getRerollToken()
                        .setQuantity(airplane.getAltitude().getRerollToken().getQuantity() - 1);
                System.out.println("Reroll token count reduced. Remaining tokens: "
                        + airplane.getAltitude().getRerollToken().getQuantity());
            }

            pilot.displayUnassignedDice();

            int selectedDiceValue = pilot.getDices().get(diceIndex).getDiceValue();
            System.out.println("You selected dice value: " + selectedDiceValue);

            System.out.println("Where do you want to place your dice?");
            System.out.println("Available options: engine, axis, radio, landing gears, brake, coffee");

            pilot.placeDice(selectedDiceValue, playerInput, fieldChoice);

            pilot.getDices().get(diceIndex).assign();

            checkTurnConditions();
        }
        // CoPilots turn
        else {
            System.out.println("It's CoPilot's turn.");
            System.out.println("These are your unassigned dice: ");
            coPilot.displayUnassignedDice();


            coPilot.displayUnassignedDice();

            int selectedDiceValue = coPilot.getDices().get(diceIndex).getDiceValue();

            coPilot.placeDice(selectedDiceValue, playerInput, fieldChoice);

            coPilot.getDices().get(diceIndex).assign();

            checkTurnConditions();

        }
    }

    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex == 0) ? 1 : 0; // 0 for pilot, 1 for copilot
        System.out.println("Next player is: " + (currentPlayerIndex == 0 ? "Pilot" : "CoPilot"));
        System.out.println(currentPlayerIndex);
    }

    public boolean checkRoundConditions() {

        if (!pilot.hasDicesOnRequiredFields() && !coPilot.hasDicesOnRequiredFields())
            return false;
        if (!airplane.getEngine().isPositionMoveSuccessful())
            return false;
        else
            return true;
    }

    public void checkTurnConditions() {

        if (airplane.getEngine().getPilotField().isFilled() && airplane.getEngine().getCoPilotField().isFilled()) {
            System.out.println("Both engine fields are filled.");
            if (!airplane.getEngine().isPositionMoveSuccessful()) {
                System.out.println("Position move failed. Ending game.");
                game.endGame();
                return;
            }
        }

        if (airplane.getAxis().getPilotAxisField().isFilled() && airplane.getAxis().getCoPilotAxisField().isFilled()) {

            if (airplane.getAxis().getCurrentTilt() == 6 || airplane.getAxis().getCurrentTilt() == 0) {
                game.endGame();
            }
        }
    }

    public void collectPlayerInput(int diceIndex, String playerInput, int fieldChoice) {

        this.currentDiceIndex = diceIndex;
        this.currentPlayerInput = playerInput;
        this.currentFieldChoice = fieldChoice;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public int getTurnsLeft() {
        return turnsLeft;
    }
    public int getCurrentDiceIndex() {
        return currentDiceIndex;
    }
    public int getCurrentFieldChoice() {
        return currentFieldChoice;
    }

    public String getCurrentPlayerInput() {
        return currentPlayerInput;
    }
}


