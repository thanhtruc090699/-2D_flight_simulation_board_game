package com.mygdx.skyteam.logic;

import java.util.List;

public class Field {
    private String name;
    private List<Integer> validDiceValues;
    private boolean isFilled;
    private Integer placedDice;

    public Field(String name, List<Integer> validDiceValues) {
        this.name = name;
        this.validDiceValues = validDiceValues;
        this.isFilled = false;
        this.placedDice = null;
    }

    public boolean setDiceValue(int diceValue) {
        if (validDiceValues.contains(diceValue)) {
            this.placedDice = diceValue;
            this.isFilled = true;
            return true;
        }
        return false;
    }

    public void resetField() {
        this.placedDice = null;
        this.isFilled = false;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public Integer getPlacedDice() {
        return placedDice;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getValidDiceValues() {
        return validDiceValues;
    }

    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

}
