package com.mygdx.skyteam.logic;

import java.util.Random;

public class Dice {
    private int value;
    private boolean assigned;
    private float x,y;

    public int rollDice() {
        Random random = new Random();
        value = random.nextInt(6) + 1;
        return value;
    }

    public void modifyValue(String input) {

        if (this.value < 1 || this.value > 6) {
            System.out.println("Dice has not been rolled yet or has an invalid value.");
            return;
        }

        if (input.equals("increase")) {
            if (this.value < 6) {
                this.value++;
                System.out.println("Dice value increased to: " + this.value);
            } else {
                System.out.println("Dice value is already at the maximum (6). It can't be increased.");
            }
        } else if (input.equals("decrease")) {
            if (this.value > 1) {
                this.value--;
                System.out.println("Dice value decreased to: " + this.value);
            } else {
                System.out.println("Dice value is already at the minimum (1). It can't be decreased.");
            }
        }

    }

    public void setDiceValue(int value) {
        this.value = value;
    }

    public int getDiceValue() {
        return value;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void assign() {
        this.assigned = true;
    }

    public void unassign() {
        this.assigned = false;
    }


}