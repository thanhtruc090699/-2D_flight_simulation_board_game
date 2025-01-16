package com.mygdx.skyteam.logic;

import java.util.ArrayList;

public class GameLogic {
    private boolean gameOver;
    private boolean hasWon;
    private Pilot pilot;
    private Airplane airplane;
    private CoPilot coPilot;
    private Round currentRound;
    private int currentRoundNumber;
    private int startingPlayerIndex = 0;

    public GameLogic() {
        airplane = new Airplane();
        currentRoundNumber = 1;
    }

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

    public void startRound() {
        currentRound = new Round(pilot, coPilot, airplane, this, startingPlayerIndex);
        /* System.out.println("ROUND : " + currentRoundNumber); */

        /* System.out.println("\nPilot's unassigned dice:"); */
        pilot.displayUnassignedDice();

        /* System.out.println("\nCoPilot's unassigned dice:"); */
        coPilot.displayUnassignedDice();

    }

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

    public void endGame() {
        if (checkWinningConditions()) {
            hasWon = true;
            /* System.err.println("You won!"); */
        } else {
            gameOver = true;
            /* System.err.println("You lost! :("); */
        }
    }

    public boolean checkWinningConditions() {

        if (airplane.getEngine().getCurrentPosition() != 6) {
            /* System.out.println("You crash-landed before reaching the airport!"); */
            return false;
        }

        if (airplane.getAxis().getCurrentTilt() != 3) {
            /* System.out.println("The plane spun around and crashed!"); */
            return false;
        }

        for (Field gear : airplane.getLandingGears().getLandingGearFields()) {
            if (!gear.isFilled()) {
                /* System.out.println("Pilot didn't deploy all landing gears."); */
                return false;
            }
        }

        for (Field flap : airplane.getFlaps().getFlapsFields()) {
            if (!flap.isFilled()) {
                /* System.out.println("CoPilot didn't activate all flaps."); */
                return false;
            }
        }

        int airplaneSpeed = airplane.getEngine().getSpeed();
        if (airplaneSpeed < airplane.getBrakes().getRedMarker()) {
            /*
             * System.out.
             * println("Your speed had to be lower than the brake value. You crashed.");
             */
            return false;
        }

        return true;
    }

    public Pilot getPilot() {
        return this.pilot;
    }

    public CoPilot getCoPilot() {
        return this.coPilot;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public Round getRound() {
        return currentRound;
    }

    public int getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    public int setCurrentRoundNumber(int x) {
        return currentRoundNumber = x;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean hasWon() {
        return hasWon;
    }

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

}
