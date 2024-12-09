package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Concentration{
    private ArrayList<Field> coffeeFields;

    public Concentration(){
        coffeeFields=new ArrayList<>();

        coffeeFields.add(new Field("Coffee Field 1", Arrays.asList(1, 2, 3, 4, 5, 6)));
        coffeeFields.add(new Field("Coffee Field 2", Arrays.asList(1, 2, 3, 4, 5, 6)));
        coffeeFields.add(new Field("Coffee Field 3", Arrays.asList(1, 2, 3, 4, 5, 6)));
    }
    
    public void fillCoffeeFields(int diceValue){
        for (Field field : coffeeFields) {
            if (!field.isFilled()) { 
                if (field.setDiceValue(diceValue)) {
                    System.out.println(field.getName() + " is now filled with dice value " + diceValue + ".");
                    return;
                } else {
                    System.out.println("Invalid dice value " + diceValue + " for " + field.getName());
                    return;
                }
            }
        }
        System.out.println("All coffee fields are already filled.");
    }
    public ArrayList<Field> getCoffeeFields() {
        return coffeeFields;
    }

    public void resetCoffeeField(int fieldIndex) {
        coffeeFields.get(fieldIndex).resetField();
    }
    
}