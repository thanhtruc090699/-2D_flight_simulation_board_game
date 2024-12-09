package com.mygdx.skyteam.logic;

import java.util.ArrayList;

public class Token {
    private String type;
    private int quantity;

    public Token(String type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

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
            System.out.println("You used one of the Coffee Tokens.");

        } else {
            System.out.println("No coffee tokens left to use.");
        }
    }

    public void useRerollToken(ArrayList<Dice> selectedDice) {
        if (quantity > 0) {
            if (selectedDice.size() > 0) {
                for (Dice dice : selectedDice) {
                    dice.rollDice();
                }

            } else {
                System.out.println("No dice selected to reroll.");
            }
        }
    }

    // setters & getters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }
}
