package com.mygdx.skyteam.logic;

import java.util.ArrayList;
/**
 * Abstract class representing a generic player in the game.
 * Provides common functionality for players, such as managing dice and tokens.
 */
public abstract class Player {
    protected String name;
    protected String role;
    protected Token coffeeTokens;
    protected ArrayList<Dice> dices;
    protected Airplane airplane;

    /**
     * Initializes a player with a name, role, and assigned airplane.
     * Creates four dice for the player and rolls them.
     *
     * @param name      the name of the player.
     * @param role      the role of the player.
     * @param airplane  the airplane associated with the player.
     */
    public Player(String name, String role, Airplane airplane){
        this.name = name;
        this.role = role;
        this.airplane = airplane;
        coffeeTokens = new Token("Coffee", 0);
        dices = new ArrayList<>();

        // Initialize four dice and roll them
        for (int i = 0; i < 4; i++) {
            Dice dice = new Dice();
            dice.rollDice();
            dice.unassign();
            dices.add(dice);
        }
    }

    /**
     * Rerolls all four dice for the player.
     */
    public void rerollDice(){
        for (int i = 0; i < 4; i++) {
            dices.get(i).rollDice();
        }
    }


    /**
     * Abstract method for placing dice on the game board.
     * Implemented by specific player types (e.g., Pilot, CoPilot).
     *
     * @param diceValue   the value of the dice to place.
     * @param playerInput the target component (e.g., "engine", "radio").
     * @param placeHolder additional parameter for field selection.
     */
    public abstract void placeDice(int diceValue, String playerInput, int placeHolder);
    /**
     * Displays the unassigned dice in the player's collection.
     * (Primarily used for debugging purposes.)
     */
    public void displayUnassignedDice() {
        /* System.out.println("Unassigned dices: "); */
        for (int i = 0; i < dices.size(); i++) {
            Dice dice = dices.get(i);
            if (!dice.isAssigned()) {
                /* System.out.println(dice.getDiceValue() + " "); */
            }
        }
    }

    /**
     * Resets all dice assignments, making them unassigned.
     */
    public void resetDiceAssignments() {
        for (Dice dice : dices) {
            dice.unassign();
        }
        /* System.out.println("All dice have been reset to unassigned."); */
    }


    /**
     * Uses a reroll token to reroll specific unassigned dice.
     */
    public void useRerollToken() {
        if (hasRerollToken()) {
            displayUnassignedDice();

            ArrayList<Dice> selectedDice = new ArrayList<>();
            String input;

            input ="1"; //hardcoded for testing

            try {
                int diceIndex = Integer.parseInt(input) - 1;


                if (diceIndex >= 0 && diceIndex < getUnassignedDice().size()) {
                    selectedDice.add(getUnassignedDice().get(diceIndex));
                    /* System.out.println("Dice " + (diceIndex + 1) + " selected."); */
                } else {
                    /* System.out.println("Invalid index. Try again."); */
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid index or 'done'.");
            }
            input = "done";  // we simulate that the user is done selecting dice.

            if (input.equals("done")) {
                airplane.getAltitude().getRerollToken().useRerollToken(selectedDice);
                }
            }
    }


    /**
     * Checks if the player has reroll tokens available.
     *
     * @return true if reroll tokens are available, false otherwise.
     */
    public boolean hasRerollToken() {
        return airplane.getAltitude().getRerollToken().getQuantity() > 0;
    }

    /**
     * Gets all dice owned by the player.
     *
     * @return an ArrayList of the player's dice.
     */
    public ArrayList<Dice> getDices(){
        return dices;
    }

    /**
     * Gets all unassigned dice from the player's collection.
     *
     * @return an ArrayList of unassigned dice.
     */
    public ArrayList<Dice> getUnassignedDice() {
        ArrayList<Dice> unassignedDices = new ArrayList<>();
        for (Dice dice : dices) {
            if (!dice.isAssigned()) {
                unassignedDices.add(dice);
            }
        }
        return unassignedDices;
    }

    /**
     * Gets the player's coffee tokens.
     *
     * @return the coffee tokens.
     */
    public Token getCoffeeToken(){
        return coffeeTokens;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name.
     */
    public String getName(){
        return name;
    }



}
