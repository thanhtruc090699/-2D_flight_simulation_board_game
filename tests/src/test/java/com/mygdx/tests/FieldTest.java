package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

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
        assertTrue(field.isFilled(), "Field should be filled after setting a valid dice value.");
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
    void testResetField() {
        field.setDiceValue(2);
        assertTrue(field.isFilled());
        field.resetField();
        assertFalse(field.isFilled());
        assertNull(field.getPlacedDice());
    }

    @Test
    void testSetFilled() {
        field.setFilled(true);
        assertTrue(field.isFilled(), "Field should be marked as filled.");

        field.setFilled(false);
        assertFalse(field.isFilled(), "Field should not be marked as filled.");
    }
    @Test
    void testIsMouseOverInsideField() {
        assertTrue(field.isMouseOver(120, 160), "Mouse should be over the field.");
    }

    @Test
    void testIsMouseOverOutsideField() {
        assertFalse(field.isMouseOver(50, 50), "Mouse should not be over the field.");
    }

    @Test
    void testGetX() {
        assertEquals(100, field.getX(), 0.0f, "X-coordinate should match the initialized value.");
    }

    @Test
    void testGetY() {
        assertEquals(150, field.getY(), 0.0f, "Y-coordinate should match the initialized value.");
    }

    @Test
    void testCanAcceptDiceValid() {
        assertTrue(field.canAcceptDice(3), "Field should accept valid dice value.");
    }

    @Test
    void testCanAcceptDiceInvalid() {
        assertFalse(field.canAcceptDice(6), "Field should not accept invalid dice value.");
    }

    @Test
    void testCanAcceptDiceWhenFilled() {
        field.setDiceValue(2);
        assertFalse(field.canAcceptDice(3), "Field should not accept dice value when already filled.");
    }

    @Test
    void testSetHighlightedTrue() {
        field.setHighlighted(true);
        assertTrue(field.isHighlighted(), "Field should be highlighted.");
    }

    @Test
    void testSetHighlightedFalse() {
        field.setHighlighted(false);
        assertFalse(field.isHighlighted(), "Field should not be highlighted.");
    }

    @Test
    void testGetColorWhenNotHighlighted() {
        assertEquals(0, field.getColor().a, "Color alpha should be 0 when not highlighted.");
    }

    @Test
    void testGetColorWhenHighlighted() {
        field.setHighlighted(true);
        field.draw(2);
        assertEquals(1f, field.getColor().a, "Color alpha should be 1 when highlighted.");
    }

    @Test
    void testGetValidDiceValues() {
        List<Integer> expectedValues = Arrays.asList(1, 2, 3, 4);
        assertEquals(expectedValues, field.getValidDiceValues(), "Valid dice values should match.");
    }

    @Test
    void testGetName() {
        assertEquals("Test Field", field.getName(), "Field name should match the initialized value.");
    }

    @Test
    void testIsBrakes() {
        Field brakeField = new Field("Brake Field", Arrays.asList(1, 2), 100, 150);
        assertTrue(brakeField.isBrakes(), "Field with 'Brake' in its name should return true for isBrakes.");
    }

    @Test
    void testIsFlaps() {
        Field flapField = new Field("Flap Field", Arrays.asList(1, 2), 100, 150);
        assertTrue(flapField.isFlaps(), "Field with 'Flap' in its name should return true for isFlaps.");
    }
}
