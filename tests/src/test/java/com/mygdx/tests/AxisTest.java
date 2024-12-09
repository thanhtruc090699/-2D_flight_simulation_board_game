package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Axis;

import static org.junit.jupiter.api.Assertions.*;

class AxisTest {
    private Axis axis;

    @BeforeEach
    void setUp(){
        axis = new Axis();
    }
    @Test
    void testInitialValues() {
        assertEquals(3, axis.getCurrentTilt(), "This should always give 3 since axis will be leveled initially");
        assertEquals(null, axis.getPilotAxisField().getPlacedDice());
        assertEquals(null, axis.getCoPilotAxisField().getPlacedDice());
    }

    @Test
    void testAdjustTiltCoPilotSide() { // Plane Tilts 90 degrees to the right.
        assertAll(
                () -> {
                    axis.placePilotDice(2);
                    axis.placeCoPilotDice(5);
                    axis.adjustTilt();
                    assertEquals(4, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(5);
                    axis.placeCoPilotDice(6);
                    axis.adjustTilt();
                    assertEquals(5, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(2);
                    axis.placeCoPilotDice(1);
                    axis.adjustTilt();
                    assertEquals(4, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(1);
                    axis.placeCoPilotDice(3);
                    axis.adjustTilt();
                    assertEquals(5, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(2);
                    axis.placeCoPilotDice(4);
                    axis.adjustTilt();
                    assertEquals(-1, axis.getCurrentTilt());
                });
    }

    @Test
    void testAdjustTiltPilotSide() { // Plane Tilts 90 degrees to the left.
        assertAll(
                () -> {
                    axis.placePilotDice(5);
                    axis.placeCoPilotDice(2);
                    axis.adjustTilt();
                    assertEquals(2, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(6);
                    axis.placeCoPilotDice(5);
                    axis.adjustTilt();
                    assertEquals(1, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(1);
                    axis.placeCoPilotDice(2);
                    axis.adjustTilt();
                    assertEquals(2, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(3);
                    axis.placeCoPilotDice(1);
                    axis.adjustTilt();
                    assertEquals(1, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(4);
                    axis.placeCoPilotDice(2);
                    axis.adjustTilt();
                    assertEquals(-1, axis.getCurrentTilt());
                });
    }

    @Test
    void testPlacePilotDice() {
        assertAll(
                () -> {
                    axis.placePilotDice(1);
                    assertEquals(1, axis.getPilotAxisField().getPlacedDice());
                },
                () -> {
                    axis.placePilotDice(4);
                    assertEquals(4, axis.getPilotAxisField().getPlacedDice());
                },
                () -> {
                    axis.placePilotDice(6);
                    assertEquals(6, axis.getPilotAxisField().getPlacedDice());
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> axis.placePilotDice(0));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> axis.placePilotDice(7));
                });
    }

    @Test
    void testPlaceCoPilotDice() {
        assertAll(
                () -> {
                    axis.placeCoPilotDice(1);
                    assertEquals(1, axis.getCoPilotAxisField().getPlacedDice());
                },
                () -> {
                    axis.placeCoPilotDice(4);
                    assertEquals(4, axis.getCoPilotAxisField().getPlacedDice());
                },
                () -> {
                    axis.placeCoPilotDice(6);
                    assertEquals(6, axis.getCoPilotAxisField().getPlacedDice());
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> axis.placeCoPilotDice(0));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> axis.placeCoPilotDice(7));
                });
    }

    @Test
    void testGetPilotDice() {
        axis.placePilotDice(1);
        assertEquals(1, axis.getPilotAxisField().getPlacedDice());

        axis.placePilotDice(6);
        assertEquals(6, axis.getPilotAxisField().getPlacedDice());

        axis.placePilotDice(3);
        assertEquals(3, axis.getPilotAxisField().getPlacedDice());
    }

    @Test
    void testGetCoPilotDice() {
        axis.placeCoPilotDice(1);
        assertEquals(1, axis.getCoPilotAxisField().getPlacedDice());

        axis.placeCoPilotDice(6);
        assertEquals(6, axis.getCoPilotAxisField().getPlacedDice());

        axis.placeCoPilotDice(3);
        assertEquals(3, axis.getCoPilotAxisField().getPlacedDice());
    }

    @Test
    void testAdjustTiltWithEqualDice() {
        axis.placePilotDice(3);
        axis.placeCoPilotDice(3);
        axis.adjustTilt();
        assertEquals(3, axis.getCurrentTilt(), "The tilt should be 3 (balanced) when both dice have equal values.");
    }


}