package com.mygdx.tests;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Airplane;
import com.mygdx.skyteam.logic.Engine;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    private Engine engine;
    private Airplane airplane;
    private ArrayList<Integer> planesOnTrack;

    @BeforeEach
    void setUp() {
        engine = new Engine();
        airplane = new Airplane();
        planesOnTrack = airplane.getPlanesOnTrack();
    }

    @Test
    void testInitialEngineState() {
        assertEquals(0, engine.getCurrentPosition(), "Initial position should be 0.");
        assertEquals(0, engine.getSpeed(), "Initial speed should be 0.");
        assertEquals(4, engine.getBlueMarker(), "Initial blue marker should be 5.");
        assertEquals(9, engine.getOrangeMarker(), "Initial orange marker should be 9.");
    }

    @Test
    void testPlacePilotDice() {
        engine.placePilotDice(4);
        assertEquals(4, engine.getPilotField().getPlacedDice());
    }

    @Test
    void testPlaceCoPilotDice() {
        engine.placeCoPilotDice(3);
        assertEquals(3, engine.getCoPilotField().getPlacedDice());
    }

    @Test
    void testShiftBlueMarker() {
        engine.shiftBlueMarker();
        assertEquals(5, engine.getBlueMarker(), "Blue marker should be incremented to 5.");
        assertEquals(0, engine.getCurrentPosition(), "Position should remain the same after shifting the blue marker.");
    }

    @Test
    void testShiftOrangeMarker() {
        engine.shiftOrangeMarker();
        assertEquals(10, engine.getOrangeMarker(), "Orange marker should be incremented to 10.");
        assertEquals(0, engine.getCurrentPosition(),
                "Position should remain the same after shifting the orange marker.");
    }

    @Test
    void testAdjustSpeedAndUpdatePositionMoveOneStep() {
        engine.placePilotDice(3);
        engine.placeCoPilotDice(3); // Total speed = 6
        engine.adjustSpeed(planesOnTrack);
        assertEquals(6, engine.getSpeed(), "Speed should be calculated as 6.");
        assertEquals(1, engine.getCurrentPosition(), "Plane should move 1 step.");
    }

    @Test
    void testAdjustSpeedAndCrashDueToPlaneOnTrack() {
        planesOnTrack.set(1, 1); // Placed a plane on track at position 1 to crash it purposefully
        engine.placePilotDice(3);
        engine.placeCoPilotDice(3); // Total speed = 6
        engine.adjustSpeed(planesOnTrack);
        assertFalse(engine.updatePosition(planesOnTrack));
        assertFalse(engine.checkPlanesOnTrack(planesOnTrack, 1));
        assertEquals(0, engine.getCurrentPosition(), "Plane should not move due to a crash.");
    }

    @Test
    void testAdjustSpeedAndMoveTwoSteps() {
        planesOnTrack.set(2, 0);
        engine.placePilotDice(5);
        engine.placeCoPilotDice(5);
        engine.adjustSpeed(planesOnTrack);
        assertEquals(10, engine.getSpeed());
        assertEquals(2, engine.getCurrentPosition());
    }

    @Test
    void testUpdatePositionWithoutMovement() {
        engine.placePilotDice(2);
        engine.placeCoPilotDice(1);
        engine.adjustSpeed(planesOnTrack);
        assertEquals(0, engine.getCurrentPosition(), "Plane should not move.");
    }

    @Test
    void testCheckWinConditionOvershoot() {
        engine.placePilotDice(6);
        engine.placeCoPilotDice(6); // High speed
        planesOnTrack.set(2, 0);
        planesOnTrack.set(3, 0);
        planesOnTrack.set(4, 0);
        planesOnTrack.set(5, 0);
        planesOnTrack.set(6, 0);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);
        assertFalse(engine.checkWinLossConditionForEngine(planesOnTrack), "Should lose for overshooting the airport.");
    }

    @Test
    void testCheckWinConditionCrashOnTrack() {
        planesOnTrack.set(1, 1);
        engine.placePilotDice(3);
        engine.placeCoPilotDice(3);
        engine.adjustSpeed(planesOnTrack);
        assertFalse(engine.checkWinLossConditionForEngine(planesOnTrack), "Should lose due to a crash on track.");
    }

    @Test
    void testCheckWinConditionSuccess() {
        planesOnTrack.set(2, 0);
        planesOnTrack.set(3, 0);
        planesOnTrack.set(4, 0);
        planesOnTrack.set(5, 0);
        planesOnTrack.set(6, 0);
        engine.placePilotDice(5);
        engine.placeCoPilotDice(5);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);
        engine.placePilotDice(1);
        engine.placeCoPilotDice(2);
        engine.adjustSpeed(planesOnTrack);
        assertTrue(engine.checkWinLossConditionForEngine(planesOnTrack));
    }

    @Test
    void testResetFields() {
        engine.placePilotDice(5);
        engine.placeCoPilotDice(4);
        assertEquals(5, engine.getPilotField().getPlacedDice());
        assertEquals(4, engine.getCoPilotField().getPlacedDice());
        engine.resetFields();
        assertNull(engine.getPilotField().getPlacedDice());
        assertNull(engine.getCoPilotField().getPlacedDice());
    }

    @Test
    void testAllTracksBlocked() {
        for (int i = 0; i < planesOnTrack.size(); i++) {
            planesOnTrack.set(i, 1); // Mark all tracks as blocked
        }
        engine.placePilotDice(2);
        engine.placeCoPilotDice(3);

        engine.adjustSpeed(planesOnTrack);

        assertFalse(engine.updatePosition(planesOnTrack), "Plane should not move because all tracks are blocked.");
    }

    @Test
    void testUpdatePositionMoveTwoSteps_Crash() {
        engine.placePilotDice(6);
        engine.placeCoPilotDice(6);
        engine.adjustSpeed(planesOnTrack);

        ArrayList<Integer> planesOnTrack = new ArrayList<>(Arrays.asList(0, 1, 1, 0));
        boolean result = engine.updatePosition(planesOnTrack);

        assertFalse(result, "Plane should crash due to occupied positions.");
        assertEquals(0, engine.getCurrentPosition(), "Position should remain 0 after crash.");
    }

    @Test
    void testPlacePilotDice_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.placePilotDice(7);
        }, "Invalid dice value for Pilot Engine Field");
    }

    @Test
    void testPlaceCoPilotDice_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.placeCoPilotDice(8);
        }, "Invalid dice value for Co-Pilot Engine Field");
    }

}