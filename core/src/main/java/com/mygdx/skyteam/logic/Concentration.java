package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Manages the coffee concentration fields in the game.
 * Allows players to fill and reset coffee fields based on dice values.
 */
public class Concentration{
    private ArrayList<Field> coffeeFields;

    /**
     * Initializes the coffee fields with default values and positions.
     */
    public Concentration(){
        coffeeFields=new ArrayList<>();

        coffeeFields.add(new Field("Coffee Field 1", Arrays.asList(1, 2, 3, 4, 5, 6),862, 1010));
        coffeeFields.add(new Field("Coffee Field 2", Arrays.asList(1, 2, 3, 4, 5, 6), 936, 1010));
        coffeeFields.add(new Field("Coffee Field 3", Arrays.asList(1, 2, 3, 4, 5, 6), 1010, 1010));
    }

    /**
     * Fills the coffee field with the given dice value.
     *
     * @param diceValue the value of the dice to fill a coffee field.
     */
    public void fillCoffeeFields(int diceValue){
        for (Field field : coffeeFields) {
            if (!field.isFilled()) {
                if (field.setDiceValue(diceValue)) {
                    /* System.out.println(field.getName() + " is now filled with dice value " + diceValue + "."); */
                    return;
                } else {
                    /* System.out.println("Invalid dice value " + diceValue + " for " + field.getName()); */
                    return;
                }
            }
        }
       /*  System.out.println("All coffee fields are already filled."); */
    }

    /**
     * Gets the list of coffee fields.
     *
     * @return the list of coffee fields.
     */
    public ArrayList<Field> getCoffeeFields() {
        return coffeeFields;
    }

    /**
     * Resets a specific coffee field by its index.
     *
     * @param fieldIndex the index of the coffee field to reset.
     */
    public void resetCoffeeField(int fieldIndex) {
        coffeeFields.get(fieldIndex).resetField();
    }

}
