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

            handleCoffeeToken(pilot);

            pilot.displayUnassignedDice();

            int selectedDiceValue = pilot.getDices().get(diceIndex).getDiceValue();
            System.out.println("You selected dice value: " + selectedDiceValue);

            System.out.println("Where do you want to place your dice?");
            System.out.println("Available options: engine, axis, radio, landing gears, brake, coffee");

            pilot.placeDice(selectedDiceValue, playerInput, fieldChoice);

            pilot.getDices().get(diceIndex).assign();

            if (airplane.getAxis().getPilotAxisField().isFilled()
                    && airplane.getAxis().getCoPilotAxisField().isFilled()) { // in case axis reaches x prematurely,
                                                                              // end game
                if (airplane.getAxis().getCurrentTilt() == 6 || airplane.getAxis().getCurrentTilt() == 1) {
                    checkRoundConditions();
                    game.endGame();
                    return;
                }
            }
        }
        // CoPilots turn
        else {
            System.out.println("It's CoPilot's turn.");
            System.out.println("These are your unassigned dice: ");
            coPilot.displayUnassignedDice();

            handleCoffeeToken(pilot);

            coPilot.displayUnassignedDice();

            int selectedDiceValue = coPilot.getDices().get(diceIndex).getDiceValue();
            System.out.println("You selected dice value: " + selectedDiceValue);

            coPilot.placeDice(selectedDiceValue, playerInput, fieldChoice);

            coPilot.getDices().get(diceIndex).assign();

            if (airplane.getAxis().getPilotAxisField().isFilled()
                    && airplane.getAxis().getCoPilotAxisField().isFilled()) { // in case axis reaches x prematurely,
                                                                              // end game
                if (airplane.getAxis().getCurrentTilt() == 6 || airplane.getAxis().getCurrentTilt() == 1) {
                    checkRoundConditions();
                    game.endGame();
                    return;
                }
            }

        }
    }

    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex == 0) ? 1 : 0; // 0 for pilot, 1 for copilot
        System.out.println("Next player is: " + (currentPlayerIndex == 0 ? "Pilot" : "CoPilot"));
        System.out.println(currentPlayerIndex);
    }

    public boolean checkRoundConditions() {

        if (!pilot.hasDicesOnRequiredFields() && !pilot.hasDicesOnRequiredFields())
            return false;
        if (!airplane.checkAirplaneConditions())
            return false;
        else
            return true;
    }

    public void handleCoffeeToken(Player player) {
        if (player.getCoffeeToken().getQuantity() >= 1) {
            System.out.println("You have a Coffee Token. Do you want to use it? (yes/no)");

            String useCoffeeToken = "no";

            if (useCoffeeToken.equals("yes")) {
                System.out.println("Choose a dice from range 1-" + player.getUnassignedDice().size());
                int diceIndex = 0;

                if (diceIndex >= 0 && diceIndex < player.getUnassignedDice().size()) {
                    player.getCoffeeToken().useCoffeeToken(player.getUnassignedDice().get(diceIndex), airplane);
                    System.out.println("Coffee Token used.");
                } else {
                    System.out.println("Invalid dice index. Please choose a valid dice.");
                }
            } else {
                System.out.println("You decided not to use the Coffee Token.");
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
}
