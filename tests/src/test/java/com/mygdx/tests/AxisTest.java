package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Axis;

import static org.junit.jupiter.api.Assertions.*;

class AxisTest {
    private Axis axis;

    @BeforeEach
    void setUp() {
        axis = new Axis();
    }

    @Test
    void testInitialValues() {
        assertEquals(3, axis.getCurrentTilt(), "This should always give 3 since axis will be leveled initially");
        assertEquals(null, axis.getPilotAxisField().getPlacedDice());
        assertEquals(null, axis.getCoPilotAxisField().getPlacedDice());
    }

    @Test
    void testAdjustTiltCoPilotSide() {
        assertAll(
                () -> {
                    axis.placePilotDice(2);
                    axis.placeCoPilotDice(5);
                    axis.adjustTilt();
                    assertEquals(6, axis.getCurrentTilt(), "Expected tilt to be 6 after adjustment.");
                },
                () -> {
                    axis.placePilotDice(5);
                    axis.placeCoPilotDice(6);
                    axis.adjustTilt();
                    assertEquals(4, axis.getCurrentTilt(), "Expected tilt to be 4 after adjustment.");
                },
                () -> {
                    axis.placePilotDice(2);
                    axis.placeCoPilotDice(1);
                    axis.adjustTilt();
                    assertEquals(2, axis.getCurrentTilt(), "Expected tilt to be 2 after adjustment.");
                },
                () -> {
                    axis.placePilotDice(1);
                    axis.placeCoPilotDice(3);
                    axis.adjustTilt();
                    assertEquals(5, axis.getCurrentTilt(), "Expected tilt to be 5 after adjustment.");
                },
                () -> {
                    axis.placePilotDice(2);
                    axis.placeCoPilotDice(4);
                    axis.adjustTilt();
                    assertEquals(5, axis.getCurrentTilt(), "Expected tilt to be 5 after adjustment.");
                });
    }

    @Test
    void testAdjustTiltPilotSide() {
        assertAll(
                () -> {
                    axis.placePilotDice(5);
                    axis.placeCoPilotDice(2);
                    axis.adjustTilt();
                    assertEquals(6, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(6);
                    axis.placeCoPilotDice(5);
                    axis.adjustTilt();
                    assertEquals(2, axis.getCurrentTilt());
                },
                () -> {
                    axis.placePilotDice(1);
                    axis.placeCoPilotDice(2);
                    axis.adjustTilt();
                    assertEquals(4, axis.getCurrentTilt());
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
                    assertEquals(1, axis.getCurrentTilt());
                });
    }

    @Test
    void testSingleAdjustTilt() {
        axis.placePilotDice(4);
        axis.placeCoPilotDice(2);
        axis.adjustTilt();
        assertEquals(1, axis.getCurrentTilt());
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

    @Test
    void testGetRotationAngle_withTilt0() {
        axis.setTilt(0);
        float angle = axis.getRotationAngle();
        assertEquals(80f, angle, "Rotation angle should be 80f for tilt 0");
    }

    @Test
    void testGetRotationAngle_withTilt1() {
        axis.setTilt(1);
        float angle = axis.getRotationAngle();
        assertEquals(60f, angle, "Rotation angle should be 60f for tilt 1");
    }

    @Test
    void testGetRotationAngle_withTilt6() {
        axis.setTilt(6);
        float angle = axis.getRotationAngle();
        assertEquals(-80f, angle, "Rotation angle should be -80f for tilt 6");
    }

    @Test
    void testGetRotationAngle_withTilt2() {
        axis.setTilt(2);
        float angle = axis.getRotationAngle();
        assertEquals(30f, angle, "Rotation angle should be 30f for tilt 2");
    }

    @Test
    void testGetRotationAngle_withTilt4() {
        axis.setTilt(4);
        float angle = axis.getRotationAngle();
        assertEquals(-30f, angle, "Rotation angle should be -30f for tilt 4");
    }

    @Test
    void testGetRotationAngle_withInvalidTilt() {
        axis.setTilt(7);
        float angle = axis.getRotationAngle();
        assertEquals(0f, angle, "Rotation angle should be 0f for invalid tilt values");
    }
}