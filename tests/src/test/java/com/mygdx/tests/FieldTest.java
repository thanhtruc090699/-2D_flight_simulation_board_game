package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.skyteam.logic.Field;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Field field;

    @BeforeEach
    void setUp() {
        List<Integer> validDiceValues = Arrays.asList(1, 2, 3, 4);
        field = new Field("Test Field", validDiceValues, 100, 150);
    }

    @Test
    void testInitialState() {
        assertFalse(field.isFilled(), "Field should not be filled initially.");
        assertNull(field.getPlacedDice(), "Placed dice should be null initially.");
    }

    @Test
    void testSetDiceValueValid() {
        boolean result = field.setDiceValue(4);

        assertTrue(result, "Valid dice value should return true.");
        assertTrue(field.isFilled(), "Field should not be filled with an invalid dice value.");
        assertEquals(4, field.getPlacedDice());

    }

    @Test
    void testSetDiceValueInvalid() {
        boolean result = field.setDiceValue(5);
        assertFalse(result, "Invalid dice value should return false.");
        assertFalse(field.isFilled(), "Field should not be filled with an invalid dice value.");
        assertNull(field.getPlacedDice());
    }

    @Test
    void testResetFields() {
        field.setDiceValue(2);
        assertTrue(field.isFilled());
        field.resetField();
        assertFalse(field.isFilled());
        assertNull(field.getPlacedDice());
    }

    @Test
    public void testIsMouseOver_insideField() {
        assertTrue(field.isMouseOver(120, 160));
    }

    @Test
    public void testIsMouseOver_outsideField() {
        assertFalse(field.isMouseOver(50, 50));
    }

    @Test
    public void testGetX() {
        assertEquals(100, field.getX(), 0.0f);
    }

    @Test
    public void testGetY() {
        assertEquals(150, field.getY(), 0.0f);
    }

}
