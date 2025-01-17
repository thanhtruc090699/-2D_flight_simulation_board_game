package com.mygdx.skyteam.logic;

import java.util.ArrayList;

/**
 * Represents a token used in the game, such as coffee tokens or reroll tokens.
 * Tokens allow players to perform special actions like modifying dice values or
 * rerolling dice.
 */
public class Token {
    private String type;
    private int quantity;

    /**
     * Initializes a token with the specified type and quantity.
     *
     * @param type     the type of the token.
     * @param quantity the initial quantity of tokens.
     */
    public Token(String type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Uses a coffee token to modify a dice value.
     * The selected coffee token must be filled, and the player can choose to
     * increase or decrease the dice value.
     *
     * @param dice     the dice to modify.
     * @param airplane the airplane to update (used to reset coffee fields).
     */
    public void useCoffeeToken(Dice dice, Airplane airplane) {
        if (quantity > 0) {
            int coffeeTokenIndex = 2; // hardcoded for testing
            System.out.println("Which Coffee Token would you like to use? (Enter 0, 1, or 2):");

            if (coffeeTokenIndex < 0 || coffeeTokenIndex > 2) {
                System.out.println("Invalid Coffee Token index. Please enter 0, 1, or 2.");
                return;
            }

            if (!airplane.getConcentration().getCoffeeFields().get(coffeeTokenIndex).isFilled()) {
                System.out.println("The selected Coffee Token is not filled yet. Please choose a filled Coffee Token.");
                return;
            }

            System.out.println("Do you want to increase or decrease the dice value? (Enter 'increase' or 'decrease')");
            String input = "increase"; // hardcoded for testing
            dice.modifyValue(input);
            quantity--;
            airplane.getConcentration().resetCoffeeField(coffeeTokenIndex);
            System.out.println("After using, Coffee Token quantity: " + quantity);
            System.out.println("You used one of the Coffee Tokens.");

        } else {
            System.out.println("No coffee tokens left to use.");
        }
    }

    /**
     * Uses a reroll token to reroll a selected set of dice.
     *
     * @param selectedDice the list of dice to reroll.
     */
    public void useRerollToken(ArrayList<Dice> selectedDice) {
        if (quantity > 0) {
            if (selectedDice.size() > 0) {
                for (Dice dice : selectedDice) {
                    dice.rollDice();
                }
                quantity--;
            } else {
                /* System.out.println("No dice selected to reroll."); */
            }
        } else {
            /* System.out.println("No reroll tokens left to use."); */
        }
    }

    // setters & getters
    /**
     * Sets the quantity of tokens.
     *
     * @param quantity the new quantity of tokens.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the quantity of tokens.
     *
     * @return the current quantity of tokens.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the type of the token.
     *
     * @return the type of the token.
     */
    public String getType() {
        return type;
    }
}
