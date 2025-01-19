package com.mygdx.skyteam.logic;
/**
 * Represents a single round in the game.
 * Manages player turns, input, and checks game conditions to proceed to the next round.
 */
public class Round {
    private int turnsLeft;
    private Pilot pilot;
    private CoPilot coPilot;
    private int currentPlayerIndex;
    private Airplane airplane;
    private GameLogic game;

    //Variables under are used for UI purposes and does not contribute to the core logic.
    private String currentPlayerInput;
    private int currentFieldChoice;
    private int currentDiceIndex;

    /**
     * Initializes a new round with the specified players, airplane, and game logic.
     *
     * @param pilot              the pilot player.
     * @param coPilot            the co-pilot player.
     * @param airplane           the airplane being controlled.
     * @param game               the main game logic.
     * @param startingPlayerIndex the index of the player starting the round.
     * Written by: Marija
     */
    public Round(Pilot pilot, CoPilot coPilot, Airplane airplane, GameLogic game, int startingPlayerIndex) {
        turnsLeft = 4;
        this.pilot = pilot;
        this.coPilot = coPilot;
        this.currentPlayerIndex = startingPlayerIndex;
        this.airplane = airplane;
        this.game = game;
    }

    /**
     * Plays a full round of the game, alternating turns between the Pilot and CoPilot.
     * Ends the round when all turns are completed.
     * Written by: Marija
     */
    public void playRound() {
        boolean turnCompleted = false;
        int lastPlayerIndex = (game.getRoundNumber() % 2 == 0) ? 0 : 1;
        while (turnsLeft > 0) {

            if (currentDiceIndex != -1 && currentPlayerInput != null && currentFieldChoice != -1) {
                playTurn(currentDiceIndex, currentPlayerInput, currentFieldChoice);

                if (currentPlayerIndex == lastPlayerIndex) {
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

    /**
     * Executes a single turn for the current player, handling dice placement and game updates.
     *
     * @param diceIndex    the index of the dice being placed.
     * @param playerInput  the action chosen by the player.
     * @param fieldChoice  the field where the dice is placed.
     *
     * Written by: Rathin
     */
    public void playTurn(int diceIndex, String playerInput, int fieldChoice) {
        // Pilots turn
        if (currentPlayerIndex == 0) {
            /* System.out.println("It's Pilot's turn."); */
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
                /*
                 * System.out.println("Reroll token count reduced. Remaining tokens: "
                 * + airplane.getAltitude().getRerollToken().getQuantity());
                 */
            }

            int selectedDiceValue = pilot.getDices().get(diceIndex).getDiceValue();

            /* System.out.println("You selected dice value: " + selectedDiceValue); */

            pilot.placeDice(selectedDiceValue, playerInput, fieldChoice);

            pilot.getDices().get(diceIndex).assign();

            checkTurnConditions();
        }
        // CoPilots turn
        else {
            /* System.out.println("It's CoPilot's turn."); */

            int selectedDiceValue = coPilot.getDices().get(diceIndex).getDiceValue();

            coPilot.placeDice(selectedDiceValue, playerInput, fieldChoice);

            coPilot.getDices().get(diceIndex).assign();

            checkTurnConditions();

        }
    }

    /**
     * Switches to the next player (Pilot or CoPilot).
     *
     * Written by: Rathin
     */
    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex == 0) ? 1 : 0; // 0 for pilot, 1 for copilot
        /*
         * System.out.println("Next player is: " + (currentPlayerIndex == 0 ? "Pilot" :
         * "CoPilot"));
         */
        System.out.println(currentPlayerIndex);
    }

    /**
     * Checks if the round's conditions are met to proceed to the next round.
     *
     * @return true if conditions are met, false otherwise.
     * Written by: Marija
     */
    public boolean checkRoundConditions() {

        if (!pilot.hasDicesOnRequiredFields() && !coPilot.hasDicesOnRequiredFields())
            return false; // Required fields are not filled
        if (!airplane.getEngine().isPositionMoveSuccessful())
            return false; // Engine movement failed
        else
            return true;
    }

    /**
     * Checks conditions at the end of a turn, such as engine and axis stability.
     * Ends the game if critical conditions are not met.
     * Written by: Marija
     */
    public void checkTurnConditions() {

        if (airplane.getEngine().getPilotField().isFilled() && airplane.getEngine().getCoPilotField().isFilled()) {
            /* System.out.println("Both engine fields are filled."); */
            if (!airplane.getEngine().isPositionMoveSuccessful()) {
                /* System.out.println("Position move failed. Ending game."); */
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

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Collects input from the player for dice placement.
     *
     * @param diceIndex    the index of the selected dice.
     * @param playerInput  the action chosen by the player.
     * @param fieldChoice  the field where the dice will be placed.
     * Written by: Marija
     */
    public void collectPlayerInput(int diceIndex, String playerInput, int fieldChoice) {

        this.currentDiceIndex = diceIndex;
        this.currentPlayerInput = playerInput;
        this.currentFieldChoice = fieldChoice;
    }

    /**
     * Gets the index of the current player.
     *
     * @return the current player index (0 for Pilot, 1 for CoPilot).
     * Written by: Marija
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Gets the number of turns left in the round.
     *
     * @return the number of turns left.
     * Written by: Marija
     */
    public int getTurnsLeft() {
        return turnsLeft;
    }

    /**
     * Gets the index of the dice selected by the player.
     *
     * @return the dice index.
     * Written by: Marija
     */
    public int getCurrentDiceIndex() {
        return currentDiceIndex;
    }

    /**
     * Gets the field choice made by the player.
     *
     * @return the selected field choice.
     * Written by: Marija
     */
    public int getCurrentFieldChoice() {
        return currentFieldChoice;
    }

    /**
     * Gets the current player input.
     *
     * @return the action chosen by the player.
     * Written by: Marija
     */
    public String getCurrentPlayerInput() {
        return currentPlayerInput;
    }
}
