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
        assertEquals(8, engine.getOrangeMarker(), "Initial orange marker should be 8.");
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
    void testSetCurrentPosition() {
        engine.setCurrentPosition(3);
        assertEquals(3, engine.getCurrentPosition(), "Current position should be updated to 3.");
    }

    @Test
    void testSetSpeed() {
        engine.setSpeed(12);
        assertEquals(12, engine.getSpeed(), "Speed should be updated to 12.");
    }

    @Test
    void testShiftBlueMarker_Boundary() {
        engine.setCurrentPosition(6);
        for (int i = 4; i <= 7; i++) {
            engine.shiftBlueMarker();
        }
        assertEquals(7, engine.getBlueMarker(), "Blue marker should not exceed 7.");
    }

    @Test
    void testShiftOrangeMarker_Boundary() {
        for (int i = 8; i <= 12; i++) {
            engine.shiftOrangeMarker();
        }
        assertEquals(12, engine.getOrangeMarker(), "Orange marker should not exceed 12.");
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
        assertEquals(9, engine.getOrangeMarker(), "Orange marker should be incremented to 9.");
        assertEquals(0, engine.getCurrentPosition(),
                "Position should remain the same after shifting the orange marker.");
    }

    @Test
    void testAdjustSpeedAndUpdatePositionMoveOneStep() {
        engine.placePilotDice(3);
        engine.placeCoPilotDice(3);
        engine.adjustSpeed(planesOnTrack);
        assertEquals(6, engine.getSpeed(), "Speed should be calculated as 6.");
        assertEquals(1, engine.getCurrentPosition(), "Plane should move 1 step.");
    }

    @Test
    void testUpdatePositionWithExactBlueMarker() {
        planesOnTrack.set(1, 0);
        engine.placePilotDice(2);
        engine.placeCoPilotDice(2);
        engine.adjustSpeed(planesOnTrack);
        assertEquals(4, engine.getSpeed(), "Speed should match blue marker.");
        assertEquals(1, engine.getCurrentPosition(), "Plane should move 1 step.");
    }

    @Test
    void testUpdatePositionWithExactOrangeMarker() {
        planesOnTrack.set(1, 0);
        planesOnTrack.set(2, 0);
        engine.placePilotDice(4);
        engine.placeCoPilotDice(4);
        engine.adjustSpeed(planesOnTrack);
        assertEquals(8, engine.getSpeed(), "Speed should match orange marker.");
        assertEquals(2, engine.getCurrentPosition(), "Plane should move 2 steps.");
    }

    @Test
    void testAdjustSpeedAndCrashDueToPlaneOnTrack() {
        planesOnTrack.set(1, 1);
        engine.placePilotDice(3);
        engine.placeCoPilotDice(3);

        engine.adjustSpeed(planesOnTrack);

        assertFalse(engine.isPositionMoveSuccessful(), "Plane should crash and not move.");
        assertFalse(engine.checkPlanesOnTrack(planesOnTrack, 1), "Position 1 should be blocked, causing a crash.");
        assertEquals(0, engine.getCurrentPosition(), "Plane's position should remain 0 after the crash.");
    }

    @Test
    void testUpdatePositionWithOvershootAtAirport() {
        for (int i = 0; i < planesOnTrack.size(); i++) {
            planesOnTrack.set(i, 0);
        }
        engine.setCurrentPosition(5);
        engine.setBlueMarker(6);
        engine.placePilotDice(2);
        engine.placeCoPilotDice(2);
        engine.adjustSpeed(planesOnTrack);
        assertEquals(4, engine.getSpeed(), "Speed should be 4.");
        assertEquals(5, engine.getCurrentPosition(), "Plane should not overshoot if speed matches blue marker.");
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
    void testUpdatePositionFailsOnBlockedTrack() {
        planesOnTrack.set(1, 1); // Block position 1
        engine.placePilotDice(3);
        engine.placeCoPilotDice(3);
        engine.adjustSpeed(planesOnTrack);
        assertEquals(0, engine.getCurrentPosition(), "Plane should not move due to blocked track.");
        assertFalse(engine.isPositionMoveSuccessful(), "Movement should fail due to an obstacle.");
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
        for (int i = 0; i < planesOnTrack.size(); i++) {
            planesOnTrack.set(i, 0);
        }
        engine.placePilotDice(6);
        engine.placeCoPilotDice(6);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);

        assertFalse(engine.isPositionMoveSuccessful(), "Should lose for overshooting the airport.");
    }

    @Test
    void testCheckWinConditionCrashOnTrack() {
        planesOnTrack.set(1, 1);
        engine.placePilotDice(3);
        engine.placeCoPilotDice(3);

        engine.adjustSpeed(planesOnTrack);

        assertEquals(0, engine.getCurrentPosition(), "Position should remain 0 after crash.");
        assertFalse(engine.isPositionMoveSuccessful(), "Should lose due to a crash on track.");
    }

    @Test
    void testCheckWinConditionSuccess() {
        for (int i = 0; i < planesOnTrack.size(); i++) {
            planesOnTrack.set(i, 0);
        }

        engine.placePilotDice(5);
        engine.placeCoPilotDice(5);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);
        engine.adjustSpeed(planesOnTrack);

        engine.placePilotDice(1);
        engine.placeCoPilotDice(2);
        engine.adjustSpeed(planesOnTrack);

        assertEquals(6, engine.getCurrentPosition(), "Plane should be at the airport.");
        assertTrue(engine.isPositionMoveSuccessful(), "Plane should successfully win the game.");
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
            planesOnTrack.set(i, 1);
        }
        engine.placePilotDice(2);
        engine.placeCoPilotDice(3);

        engine.adjustSpeed(planesOnTrack);
        assertEquals(0, engine.getCurrentPosition(), "Plane should not move because all tracks are blocked.");
    }

    @Test
    void testUpdatePositionMoveTwoSteps_Crash() {
        engine.placePilotDice(6);
        engine.placeCoPilotDice(6);
        ArrayList<Integer> planesOnTrack = new ArrayList<>(Arrays.asList(0, 1, 1, 0));

        engine.adjustSpeed(planesOnTrack);

        assertEquals(0, engine.getCurrentPosition(), "Position should remain 0 after crash.");
        assertFalse(engine.isPositionMoveSuccessful(), "Plane should not move due to occupied positions.");
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
