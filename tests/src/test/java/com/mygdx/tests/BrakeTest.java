package com.mygdx.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.mygdx.skyteam.logic.Brake;

import static org.junit.jupiter.api.Assertions.*;

class BrakeTest {

    private Brake brake;

    @BeforeEach
    void setUp() {
        brake = new Brake();
    }

    @Test
    void testInitialValues() {
        assertEquals(0, brake.getRedMarker());
        assertAll(
                () -> assertFalse(brake.getBrakeFields().get(0).isFilled()),
                () -> assertFalse(brake.getBrakeFields().get(1).isFilled()),
                () -> assertFalse(brake.getBrakeFields().get(2).isFilled()));
    }

    @Test
    void testDeployBrakeField1() {
        brake.deployBrakes(2);
        assertTrue(brake.getBrakeFields().get(0).isFilled());
        assertEquals(2, brake.getRedMarker());
    }

    @Test
    void testDeployBrakeField2WithoutField1() {
        brake.deployBrakes(4);
        assertFalse(brake.getBrakeFields().get(1).isFilled());
        assertEquals(0, brake.getRedMarker());
    }

    @Test
    void testDeployBrakeField2WithField1() {
        brake.deployBrakes(2);
        brake.deployBrakes(4);
        assertTrue(brake.getBrakeFields().get(0).isFilled());
        assertTrue(brake.getBrakeFields().get(1).isFilled());
        assertEquals(4, brake.getRedMarker());
    }

    @Test
    void testDeployBrakeField3WithoutField2() {
        brake.deployBrakes(2);
        brake.deployBrakes(6);
        assertFalse(brake.getBrakeFields().get(2).isFilled());
        assertEquals(2, brake.getRedMarker());
    }

    @Test
    void testDeployBrakeField3WithField2() {
        brake.deployBrakes(2);
        brake.deployBrakes(4);
        brake.deployBrakes(6);
        assertTrue(brake.getBrakeFields().get(0).isFilled());
        assertTrue(brake.getBrakeFields().get(1).isFilled());
        assertTrue(brake.getBrakeFields().get(2).isFilled());
        assertEquals(6, brake.getRedMarker());
    }

    @Test
    void testDeployBrakeField1Twice() {
        brake.deployBrakes(2);
        brake.deployBrakes(2);
        assertTrue(brake.getBrakeFields().get(0).isFilled());
        assertEquals(2, brake.getRedMarker());
    }

    @Test
    void testDeployBrakeInvalidInput() {
        brake.deployBrakes(5);
        assertAll(
                () -> assertFalse(brake.getBrakeFields().get(0).isFilled()),
                () -> assertFalse(brake.getBrakeFields().get(1).isFilled()),
                () -> assertFalse(brake.getBrakeFields().get(2).isFilled()));
        assertEquals(0, brake.getRedMarker());
    }

    @Test
    void testSetRedMarker() {
        assertAll(
                () -> {
                    brake.setRedMarker(4);
                    assertEquals(4, brake.getRedMarker());
                },
                () -> {
                    brake.setRedMarker(2);
                    assertEquals(2, brake.getRedMarker());
                },
                () -> {
                    brake.setRedMarker(6);
                    assertEquals(6, brake.getRedMarker());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> brake.setRedMarker(0));
                    assertEquals("Not a valid input", exception.getMessage());
                },
                () -> assertThrows(IllegalArgumentException.class, () -> brake.setRedMarker(1)),
                () -> assertThrows(IllegalArgumentException.class, () -> brake.setRedMarker(7)));

    }

    @Test
    void testCanPlaceBrakes() {
        assertAll(
                () -> {
                    assertFalse(brake.canPlaceBrakes(1));
                },
                () -> {
                    assertFalse(brake.canPlaceBrakes(3));
                },
                () -> {
                    brake.getBrakeFields().get(0).setFilled(false);
                    assertTrue(brake.canPlaceBrakes(2));
                },
                () -> {
                    brake.getBrakeFields().get(0).setFilled(true);
                    assertFalse(brake.canPlaceBrakes(2));
                },
                () -> {
                    brake.getBrakeFields().get(0).setFilled(true);
                    brake.getBrakeFields().get(1).setFilled(false);
                    assertTrue(brake.canPlaceBrakes(4));
                },
                () -> {
                    brake.getBrakeFields().get(0).setFilled(false);
                    assertFalse(brake.canPlaceBrakes(4));
                },
                () -> {
                    brake.getBrakeFields().get(0).setFilled(true);
                    brake.getBrakeFields().get(1).setFilled(true);
                    brake.getBrakeFields().get(2).setFilled(false);
                    assertTrue(brake.canPlaceBrakes(6));
                },
                () -> {
                    brake.getBrakeFields().get(0).setFilled(false);
                    assertFalse(brake.canPlaceBrakes(6));
                },
                () -> {
                    brake.getBrakeFields().get(0).setFilled(true);
                    brake.getBrakeFields().get(1).setFilled(false);
                    assertFalse(brake.canPlaceBrakes(6));
                });
    }
    @Test
    void testCanPlaceBrakesForDice() {
        // Brake field 1
        assertTrue(brake.canPlaceBrakesForDice(2), "Brake field 1 should be placeable");
        brake.deployBrakes(2);

        // Brake field 2
        assertTrue(brake.canPlaceBrakesForDice(4), "Brake field 2 should be placeable after field 1");
        brake.deployBrakes(4);

        // Brake field 3
        assertTrue(brake.canPlaceBrakesForDice(6), "Brake field 3 should be placeable after field 1 and field 2");
        brake.deployBrakes(6);

        // Invalid inputs
        assertFalse(brake.canPlaceBrakesForDice(1), "Brake should not be placeable for dice value 1");
        assertFalse(brake.canPlaceBrakesForDice(3), "Brake should not be placeable for dice value 3");
        assertFalse(brake.canPlaceBrakesForDice(5), "Brake should not be placeable for dice value 5");
        assertFalse(brake.canPlaceBrakesForDice(7), "Brake should not be placeable for dice value 7");
    }


}
