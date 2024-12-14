package com.mygdx.skyteam.logic;

public class Game {
    private boolean gameOver;
    private Pilot pilot;
    private Airplane airplane;
    private CoPilot coPilot;
    private Round currentRound;
    private int currentRoundNumber;

    public Game() {
        airplane = new Airplane();
        currentRoundNumber = 1;
        startGame();
    }

    public void startGame() {

        System.out.println("Welcome To The Game");

        String name1 = "Player 1"; // hardcoded names for testing
        String name2 = "Player 2";

        System.out.println("Enter role for " + name1 + " : (1 for Pilot, 2 for CoPilot)");
        int roleChoice1 = 1; // again replaced with hardcoded value for testing

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
        currentRound = new Round(pilot, coPilot, airplane, this);
        System.out.println("ROUND : " + currentRoundNumber);

        System.out.println("\nPilot's unassigned dice:");
        pilot.displayUnassignedDice();

        System.out.println("\nCoPilot's unassigned dice:");
        coPilot.displayUnassignedDice();

        currentRound.playRound();

        nextRound();
    }

    public void nextRound() {

        boolean stillAlive = currentRound.checkRoundConditions();

        if (stillAlive && currentRoundNumber < 7) {
            currentRoundNumber++;

            pilot.resetDiceAssignments();
            coPilot.resetDiceAssignments();

            pilot.rerollDice();
            coPilot.rerollDice();

            airplane.getEngine().resetFields();
            airplane.getAxis().resetFields();

            pilot.getRadio().resetFields();
            coPilot.getRadio().resetFields();

            airplane.getAltitude().adjustAltitude(currentRoundNumber);
            startRound();
        } else if (!stillAlive) {
            endGame();
        } else {
            currentRoundNumber++;

            pilot.resetDiceAssignments();
            coPilot.resetDiceAssignments();

            pilot.rerollDice();
            coPilot.rerollDice();

            pilot.getRadio().resetFields();
            coPilot.getRadio().resetFields();
            airplane.getAltitude().adjustAltitude(currentRoundNumber);
            startRound();

            System.out.println("Final round completed. Ending the game.");
            endGame();
        }
    }

    public void endGame() {
        if (currentRoundNumber == 7 && checkWinningConditions()) {
            System.err.println("You won!");
        } else {
            gameOver = true;
            System.err.println("You lost! :(");
        }
    }

    public boolean checkWinningConditions() {

        if (airplane.getEngine().getCurrentPosition() != 6) {
            System.out.println("You crash-landed before reaching the airport!");
            return false;
        }

        if (airplane.getAxis().getCurrentTilt() != 3) {
            System.out.println("The plane spun around and crashed!");
            return false;
        }

        for (Field gear : airplane.getLandingGears().getLandingGearFields()) {
            if (!gear.isFilled()) {
                System.out.println("Pilot didn't deploy all landing gears.");
                return false;
            }
        }

        for (Field flap : airplane.getFlaps().getFlapsFields()) {
            if (!flap.isFilled()) {
                System.out.println("CoPilot didn't activate all flaps.");
                return false;
            }
        }

        int airplaneSpeed = airplane.getEngine().getSpeed();
        if (airplaneSpeed > airplane.getBrakes().getRedMarker()) {
            System.out.println("Your speed had to be lower than the brake value. You crashed.");
            return false;
        }

        return true;
    }

    public Pilot getPilot(){
        return this.pilot;
    } 
    public CoPilot getCoPilot(){
        return this.coPilot;
    } 
}
