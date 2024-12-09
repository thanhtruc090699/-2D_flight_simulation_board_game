package com.mygdx.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.mygdx.skyteam.logic.Dice;

import static org.junit.jupiter.api.Assertions.*;

public class DiceTest {

    Dice dice;

    @BeforeEach
    void setUp() {
        dice = new Dice();
    }

    @Test
    void testRollDice() {
        for (int i = 0; i < 100; i++) {
            int rolledValue = dice.rollDice();
            assertTrue(rolledValue >= 1 && rolledValue <= 6);
        }
    }

    @Test
    void testInitialDiceValue() {
        assertEquals(0, dice.getDiceValue());
    }

    @Test
    void testModifyValueIncrease() {
        dice.rollDice();
        int initialValue = dice.getDiceValue();

        if (initialValue < 6) {
            dice.modifyValue("increase");
            assertEquals(initialValue + 1, dice.getDiceValue());
        } else {
            dice.modifyValue("increase");
            assertEquals(6, dice.getDiceValue());
        }
    }

    @Test
    void testModifyValueIncreaseAtMax() {
        dice.rollDice();

        dice.setDiceValue(6);

        dice.modifyValue("increase");

        assertEquals(6, dice.getDiceValue(),
                "Dice value should remain at 6 as it can't be increased beyond the maximum.");
    }

    @Test
    void testModifyValueDecrease() {
        dice.rollDice();
        int initialValue = dice.getDiceValue();

        if (initialValue > 1) {
            dice.modifyValue("decrease");
            assertEquals(initialValue - 1, dice.getDiceValue());
        } else {
            dice.modifyValue("decrease");
            assertEquals(1, dice.getDiceValue());
        }
    }

    @Test
    void testModifyValueDecreaseAtMin() {
        dice.rollDice();

        dice.setDiceValue(1);

        dice.modifyValue("decrease");

        assertEquals(1, dice.getDiceValue(),
                "Dice value should remain at 1 as it can't be decreased below the minimum.");
    }

    @Test
    void testModifyValueWithInvalidInput() {
        dice.rollDice();
        int initialValue = dice.getDiceValue();

        dice.modifyValue("invalid");
        assertEquals(initialValue, dice.getDiceValue());
    }

    @Test
    void testRollDiceProducesDifferentValues() {
        boolean differentValuesFound = false;
        int firstRoll = dice.rollDice();

        for (int i = 0; i < 50; i++) {
            int newRoll = dice.rollDice();
            if (newRoll != firstRoll) {
                differentValuesFound = true;
                break;
            }
        }

        assertTrue(differentValuesFound, "Dice should produce different values across multiple rolls.");
    }

    @Test
    void testAssign() {
        assertFalse(dice.isAssigned());
        dice.assign();
        assertTrue(dice.isAssigned());
    }

    @Test
    void testUnassign() {
        dice.assign();
        assertTrue(dice.isAssigned());
        dice.unassign();
        assertFalse(dice.isAssigned());
    }

    @Test
    void testDiceValueDoesNotChangeWithoutRoll() {
        int initialValue = dice.getDiceValue();
        assertEquals(0, initialValue);
        dice.modifyValue("increase");
        assertEquals(0, dice.getDiceValue());
    }

}
