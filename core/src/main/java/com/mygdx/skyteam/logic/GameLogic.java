package com.mygdx.skyteam.logic;

import java.util.ArrayList;
/**
 * Manages the main game logic, including rounds, player actions, and win/loss conditions.
 * Tracks the game's state and coordinates interactions between the airplane and players.
 */
public class GameLogic {
    private boolean gameOver;
    private boolean hasWon;
    private Pilot pilot;
    private Airplane airplane;
    private CoPilot coPilot;
    private Round currentRound;
    private int currentRoundNumber;
    private int startingPlayerIndex = 0;

    /**
     * Initializes the game logic and sets up the airplane.
     */
    public GameLogic() {
        airplane = new Airplane();
        currentRoundNumber = 1;
    }

    /**
     * Starts the game by assigning roles to players and starting the first round.
     *
     * Written by: Rathin
     */
    public void startGame() {

        /* System.out.println("Welcome To The Game"); */

        // We didn't make name/role choosing an option in UI so it stays like this
        String name1 = "Player 1";
        String name2 = "Player 2";

        /*
         * System.out.println("Enter role for " + name1 +
         * " : (1 for Pilot, 2 for CoPilot)");
         */
        int roleChoice1 = 1;

        if (roleChoice1 == 1) {
            pilot = new Pilot(name1, airplane);
            coPilot = new CoPilot(name2, airplane);
        } else {
            pilot = new Pilot(name2, this.airplane);
            coPilot = new CoPilot(name1, this.airplane);
        }

        startRound();
    }

    /**
     * Starts a new round and displays the unassigned dice for both players.
     *
     * Written by: Rathin
     */
    public void startRound() {
        currentRound = new Round(pilot, coPilot, airplane, this, startingPlayerIndex);
        /* System.out.println("ROUND : " + currentRoundNumber); */

        /* System.out.println("\nPilot's unassigned dice:"); */
        pilot.displayUnassignedDice();

        /* System.out.println("\nCoPilot's unassigned dice:"); */
        coPilot.displayUnassignedDice();

    }

    /**
     * Proceeds to the next round if conditions are met.
     * Resets fields and dice for the new round, or ends the game if all rounds are complete.
     * Written by: Marija
     */
    public void nextRound() {

        boolean stillAlive = currentRound.checkRoundConditions();

        if (stillAlive) {
            currentRoundNumber++;
            if (currentRoundNumber < 7) {

                pilot.resetDiceAssignments();
                coPilot.resetDiceAssignments();

                pilot.rerollDice();
                coPilot.rerollDice();

                airplane.getEngine().resetFields();
                airplane.getAxis().resetFields();

                pilot.getRadio().resetFields();
                coPilot.getRadio().resetFields();

                airplane.getAltitude().adjustAltitude(currentRoundNumber);

                startingPlayerIndex = (startingPlayerIndex == 0) ? 1 : 0;

                startRound();
            } else if (currentRoundNumber == 7) {

                pilot.resetDiceAssignments();
                coPilot.resetDiceAssignments();

                pilot.rerollDice();
                coPilot.rerollDice();

                airplane.getEngine().resetFields();
                airplane.getAxis().resetFields();

                pilot.getRadio().resetFields();
                coPilot.getRadio().resetFields();

                airplane.getAltitude().adjustAltitude(currentRoundNumber);

                startingPlayerIndex = (startingPlayerIndex == 0) ? 1 : 0;

                startRound();

            }
            else{
                endGame();
            }

        } else {

            endGame();
        }
    }

    /**
     * Ends the game and determines if the player has won or lost.
     * Written by: Marija
     */
    public void endGame() {
        if (checkWinningConditions()) {
            hasWon = true;
            /* System.err.println("You won!"); */
        } else {
            gameOver = true;
            /* System.err.println("You lost! :("); */
        }
    }

    /**
     * Checks the conditions to determine if the player has won.
     *
     * @return true if the player meets all winning conditions, false otherwise.
     * Written by: Marija
     */
    public boolean checkWinningConditions() {

        // Check if the airplane has reached the airport (position 6)
        if (airplane.getEngine().getCurrentPosition() != 6) {
            /* System.out.println("You crash-landed before reaching the airport!"); */
            return false;
        }

        // Check if the airplane's tilt is stable (must be exactly 3)
        if (airplane.getAxis().getCurrentTilt() != 3) {
            /* System.out.println("The plane spun around and crashed!"); */
            return false;
        }

        // Check if all landing gears are deployed
        for (Field gear : airplane.getLandingGears().getLandingGearFields()) {
            if (!gear.isFilled()) {
                /* System.out.println("Pilot didn't deploy all landing gears."); */
                return false;
            }
        }

        // Check if all flaps are activated
        for (Field flap : airplane.getFlaps().getFlapsFields()) {
            if (!flap.isFilled()) {
                /* System.out.println("CoPilot didn't activate all flaps."); */
                return false;
            }
        }

        // Check if the airplane's speed is within the safe range for braking
        int airplaneSpeed = airplane.getEngine().getSpeed();
        if (airplaneSpeed > airplane.getBrakes().getRedMarker()) {
            /*
             * System.out.
             * println("Your speed had to be lower than the brake value. You crashed.");
             */
            return false;
        }

        return true;
    }


    /**
     * Gets the pilot player.
     *
     * @return the pilot.
     * Written by: Marija
     */
    public Pilot getPilot() {
        return this.pilot;
    }

    /**
     * Gets the co-pilot player.
     *
     * @return the co-pilot.
     * Written by: Marija
     */
    public CoPilot getCoPilot() {
        return this.coPilot;
    }

    /**
     * Gets the airplane being controlled.
     *
     * @return the airplane.
     * Written by: Marija
     */
    public Airplane getAirplane() {
        return airplane;
    }

    /**
     * Gets the current round being played.
     *
     * @return the current round.
     * Written by: Marija
     */
    public Round getRound() {
        return currentRound;
    }

    /**
     * Gets the current round number.
     *
     * @return the current round number.
     * Written by: Marija
     */
    public int getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise.
     * Written by: Marija
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Checks if the player has won.
     *
     * @return true if the player has won, false otherwise.
     * Written by: Marija
     */
    public boolean hasWon() {
        return hasWon;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Gets all fields associated with the pilot.
     *
     * @return a list of the pilot's fields.
     * Written by: Marija
     */
    public ArrayList<Field> getAllFieldsForPilot() {
        ArrayList<Field> pilotFields = new ArrayList<>();
        pilotFields.addAll(pilot.getRadio().getRadioFields());
        pilotFields.addAll(airplane.getLandingGears().getLandingGearFields());
        pilotFields.addAll(airplane.getBrakes().getBrakeFields());
        pilotFields.addAll(airplane.getConcentration().getCoffeeFields());

        // Add single fields
        pilotFields.add(airplane.getEngine().getPilotField());
        pilotFields.add(airplane.getAxis().getPilotAxisField());

        return pilotFields;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Gets all fields associated with the co-pilot.
     *
     * @return a list of the co-pilot's fields.
     * Written by: Marija
     */
    public ArrayList<Field> getAllFieldsForCoPilot() {
        ArrayList<Field> coPilotFields = new ArrayList<>();

        coPilotFields.addAll(coPilot.getRadio().getRadioFields());
        coPilotFields.addAll(airplane.getFlaps().getFlapsFields());
        coPilotFields.addAll(airplane.getConcentration().getCoffeeFields());

        // Add single fields
        coPilotFields.add(airplane.getEngine().getCoPilotField());
        coPilotFields.add(airplane.getAxis().getCoPilotAxisField());

        return coPilotFields;
    }

    public int getRoundNumber() {
        return currentRoundNumber;
    }

    public void setCurrentRoundNumber(int number){
        this.currentRoundNumber = number;
    }

}
