package com.mygdx.skyteam.logic;

import java.util.ArrayList;

public abstract class Player {
    protected String name;
    protected String role;
    protected Token coffeeTokens;
    protected ArrayList<Dice> dices;
    protected Airplane airplane;

    public Player(String name, String role, Airplane airplane){
        this.name = name;
        this.role = role;
        this.airplane = airplane;
        coffeeTokens = new Token("Coffee", 0);
        dices = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Dice dice = new Dice(); 
            dice.rollDice();
            dice.unassign();  //so each dice is marked as unassigned(not placed initially)
            dices.add(dice);
        }
    }

    public void rerollDice(){
        for (int i = 0; i < 4; i++) {
            dices.get(i).rollDice();
        }
    }


    public abstract void placeDice(int diceValue, String playerInput, int placeHolder);

    public void displayUnassignedDice() {
        System.out.println("Unassigned dices: ");
        for (int i = 0; i < dices.size(); i++) {
            Dice dice = dices.get(i);
            if (!dice.isAssigned()) {  // check if the dice is unassigned
                System.out.println(dice.getDiceValue() + " ");
            }
        }
        System.out.println();
    }

    public void resetDiceAssignments() {
        for (Dice dice : dices) {
            dice.unassign();  
        }
        System.out.println("All dice have been reset to unassigned.");
    }
    

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
                    System.out.println("Dice " + (diceIndex + 1) + " selected.");
                } else {
                    System.out.println("Invalid index. Try again.");
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

    
    public boolean hasRerollToken() {
        return airplane.getAltitude().getRerollToken().getQuantity() > 0;
    }
    public ArrayList<Dice> getDices(){
        return dices;
    }

    public ArrayList<Dice> getUnassignedDice() {
        ArrayList<Dice> unassignedDices = new ArrayList<>();
        for (Dice dice : dices) {
            if (!dice.isAssigned()) {  
                unassignedDices.add(dice);
            }
        }
        return unassignedDices;
    }

    public Token getCoffeeToken(){
        return coffeeTokens;
    }

    public String getName(){
        return name;
    }



}