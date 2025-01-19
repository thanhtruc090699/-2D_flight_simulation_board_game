package com.mygdx.skyteam.logic;

import java.util.Random;

/**
 * Represents a dice that can be rolled and modified.
 * Tracks its value, assignment status, and position.
 */
public class Dice {
    private int value;
    private boolean assigned;

    /**
     * Rolls the dice to generate a random value between 1 and 6.
     *
     * @return the new dice value.
     *
     * Written by: Rathin
     */
    public int rollDice() {
        Random random = new Random();
        value = random.nextInt(6) + 1;
        return value;
    }

    /**
     * Modifies the current dice value by increasing or decreasing it.
     * Ensures the value remains within the valid range (1 to 6).
     *
     * @param input "increase" to increment the value, "decrease" to decrement it.
     *
     * Written by: Rathin
     */
    public void modifyValue(String input) {

        if (this.value < 1 || this.value > 6) {
            /* System.out.println("Dice has not been rolled yet or has an invalid value."); */
            return;
        }

        if (input.equals("increase")) {
            if (this.value < 6) {
                this.value++;
                /* System.out.println("Dice value increased to: " + this.value); */
            } else {
                /* System.out.println("Dice value is already at the maximum (6). It can't be increased."); */
            }
        } else if (input.equals("decrease")) {
            if (this.value > 1) {
                this.value--;
                /* System.out.println("Dice value decreased to: " + this.value); */
            } else {
                /* System.out.println("Dice value is already at the minimum (1). It can't be decreased."); */
            }
        }

    }

    /**
     * Sets the value of the dice.
     *
     * @param value the new value of the dice (1 to 6).
     * Written by: Rathin
     */
    public void setDiceValue(int value) {
        this.value = value;
    }

    /**
     * Gets the current value of the dice.
     *
     * @return the dice value.
     *
     * Written by: Rathin
     */
    public int getDiceValue() {
        return value;
    }

    /**
     * Checks if the dice is assigned.
     *
     * @return true if assigned, false otherwise.
     * Written by: Marija
     */
    public boolean isAssigned() {
        return assigned;
    }

    /**
     * Marks the dice as assigned.
     * Written by: Marija
     */
    public void assign() {
        this.assigned = true;
    }

    /**
     * Marks the dice as unassigned.
     * Written by: Marija
     */
    public void unassign() {
        this.assigned = false;
    }


}
