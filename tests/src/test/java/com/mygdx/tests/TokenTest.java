package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Dice;
import com.mygdx.skyteam.logic.Airplane;
import com.mygdx.skyteam.logic.Token;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    private Dice dice;
    private Airplane airplane;
    private ArrayList<Dice> selectedDices;
    private Token rerollToken;
    private Token coffeeToken;

    @BeforeEach
    void setUp() {

        dice = new Dice();
        dice.rollDice();
        airplane = new Airplane();
        selectedDices = new ArrayList<Dice>();
        rerollToken = new Token("reroll", 2);
        coffeeToken = new Token("coffee", 3);

    }

    @Test
    void testUseCoffeeToken() {
        assertAll(
                () -> {

                    airplane.getConcentration().getCoffeeFields().get(0).setFilled(true);
                    airplane.getConcentration().getCoffeeFields().get(1).setFilled(true);
                    airplane.getConcentration().getCoffeeFields().get(2).setFilled(true);

                    assertTrue(airplane.getConcentration().getCoffeeFields().get(0).isFilled());
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(1).isFilled());
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(2).isFilled());

                    assertEquals(3, coffeeToken.getQuantity());

                    coffeeToken.useCoffeeToken(dice, airplane);

                    assertEquals(2, coffeeToken.getQuantity());
                });
    }

    @Test
    void testUseRerollToken() {
        assertAll(
                () -> {

                    // set up when selectedDices are 4
                    for (int i = 0; i < 4; i++) {
                        Dice dice = new Dice();
                        dice.rollDice();
                        dice.unassign(); // so each dice is marked as unassigned(not placed initially)
                        selectedDices.add(dice);
                    }
                    rerollToken.useRerollToken(selectedDices);
                    assertEquals(1, rerollToken.getQuantity());

                },
                () -> { // use all the reroll Token

                    // set up when selectedDices are 3
                    for (int i = 0; i < 3; i++) {
                        Dice dice = new Dice();
                        dice.rollDice();
                        dice.unassign(); // so each dice is marked as unassigned(not placed initially)
                        selectedDices.add(dice);
                    }
                    rerollToken.useRerollToken(selectedDices);
                    assertEquals(0, rerollToken.getQuantity());
                },
                () -> { // no more left to use
                    for (int i = 0; i < 2; i++) {
                        Dice dice = new Dice();
                        dice.rollDice();
                        dice.unassign(); // so each dice is marked as unassigned(not placed initially)
                        selectedDices.add(dice);
                    }
                    rerollToken.useRerollToken(selectedDices);
                    assertEquals(0, rerollToken.getQuantity());
                }

        );

    }

    @Test
    void testInvalidSelectedDicesToUseRerollToken() {

        rerollToken.useRerollToken(selectedDices);
        assertEquals(2, rerollToken.getQuantity());

    }

    @Test
    void testInvalidCoffeeTokenIndex() {
        int invalidIndex = 3;
        airplane.getConcentration().getCoffeeFields().get(0).setFilled(true);
        coffeeToken.useCoffeeToken(dice, airplane);
        assertEquals(3, coffeeToken.getQuantity());
    }

    @Test
    void testGetQuantity() {
        coffeeToken.getQuantity();
        rerollToken.getQuantity();
        assertEquals(3, coffeeToken.getQuantity());
        assertEquals(2, rerollToken.getQuantity());
    }

    @Test
    void testSetQuantity() {
        coffeeToken.setQuantity(2);
        rerollToken.setQuantity(3);
        assertEquals(2, coffeeToken.getQuantity());
        assertEquals(3, rerollToken.getQuantity());
    }

    @Test
    void testGetType() {
        coffeeToken.getType();
        rerollToken.getType();
        assertEquals("coffee", coffeeToken.getType());
        assertEquals("reroll", rerollToken.getType());
    }
}