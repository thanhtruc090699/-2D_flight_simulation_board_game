package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Airplane;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AirplaneTest {
    private Airplane airplane;

    @BeforeEach
    void setUp() {
        airplane = new Airplane();
    }

    @Test
    void testAdjustAxis() {
        assertAll(
                () -> {
                    assertEquals(3, airplane.getAxis().getCurrentTilt());
                    airplane.getAxis().placeCoPilotDice(3);
                    airplane.getAxis().placePilotDice(4);
                    airplane.adjustTilt();
                    assertEquals(2, airplane.getAxis().getCurrentTilt());
                },
                () -> {
                    airplane.getAxis().resetFields();
                    airplane.getAxis().placeCoPilotDice(3);
                    airplane.getAxis().placePilotDice(4);
                    airplane.adjustTilt();
                    assertEquals(1, airplane.getAxis().getCurrentTilt());
                },
                () -> {
                    airplane.getAxis().resetFields();
                    airplane.getAxis().placeCoPilotDice(3);
                    airplane.getAxis().placePilotDice(4);
                    airplane.adjustTilt();
                    assertEquals(0, airplane.getAxis().getCurrentTilt());
                });
    }

    @Test
    void testAdjustSpeed() {
        assertAll(
                () -> {
                    assertEquals(0, airplane.getEngine().getSpeed());
                    assertEquals(0, airplane.getEngine().getCurrentPosition());
                    assertEquals(4, airplane.getEngine().getBlueMarker());
                    assertEquals(8, airplane.getEngine().getOrangeMarker());
                    airplane.getEngine().placeCoPilotDice(3);
                    airplane.getEngine().placePilotDice(4);
                    airplane.adjustSpeed();
                    assertEquals(7, airplane.getEngine().getSpeed());
                    assertEquals(1, airplane.getEngine().getCurrentPosition());
                },
                () -> {
                    airplane.getEngine().resetFields();
                    airplane.getAxis().resetFields();
                    airplane.getEngine().placeCoPilotDice(6);
                    airplane.getEngine().placePilotDice(6);
                    airplane.adjustSpeed();
                    assertEquals(12, airplane.getEngine().getSpeed());
                    assertEquals(1, airplane.getEngine().getCurrentPosition());
                });
    }

    @Test
    void testAdjustEngineAndMove() {
        airplane.getEngine().placePilotDice(5);
        airplane.getEngine().placeCoPilotDice(3);
        airplane.adjustSpeed();
        assertEquals(8, airplane.getEngine().getSpeed());
        assertEquals(1, airplane.getEngine().getCurrentPosition());

        airplane.getPlanesOnTrack().set(2, 0);
        airplane.getPlanesOnTrack().set(3, 0);
        airplane.getEngine().placePilotDice(6);
        airplane.getEngine().placeCoPilotDice(5);
        airplane.adjustSpeed();
        assertEquals(11, airplane.getEngine().getSpeed());
        assertEquals(3, airplane.getEngine().getCurrentPosition());
    }

    @Test
    void testAdjustSpeedAndCrash() {
        airplane.getPlanesOnTrack().set(1, 1);
        airplane.getEngine().placePilotDice(3);
        airplane.getEngine().placeCoPilotDice(3);
        airplane.adjustSpeed();

        assertEquals(0, airplane.getEngine().getCurrentPosition());
        assertEquals(6, airplane.getEngine().getSpeed());
    }

    @Test
    void testGetEngine() {
        airplane.getEngine();
        assertEquals(0, airplane.getEngine().getSpeed());
        assertEquals(0, airplane.getEngine().getCurrentPosition());
        assertEquals(4, airplane.getEngine().getBlueMarker());
        assertEquals(8, airplane.getEngine().getOrangeMarker());
    }

    @Test

    void testGetAxis() {
        airplane.getAxis();
        assertEquals(3, airplane.getAxis().getCurrentTilt());
    }

    @Test
    void testGetFlaps() {
        airplane.getFlaps();
        assertFalse(airplane.getFlaps().getFlapsFields().isEmpty());
    }

    @Test
    void testGetLandingGear() {
        airplane.getLandingGears();
        assertFalse(airplane.getLandingGears().getLandingGearFields().isEmpty());
    }

    @Test
    void testGetPlanesOnTrack() {
        ArrayList<Integer> planesOnTrack = new ArrayList<Integer>();
        planesOnTrack.add(0); // 7 fields to airport, each index is position and contains the number of planes
                              // on it, this is the original game track
        planesOnTrack.add(0);
        planesOnTrack.add(1);
        planesOnTrack.add(2);
        planesOnTrack.add(1);
        planesOnTrack.add(3);
        planesOnTrack.add(2);
        assertEquals(planesOnTrack, airplane.getPlanesOnTrack());
    }

    @Test
    void testGetConcentration() {
        airplane.getConcentration();
        assertFalse(airplane.getConcentration().getCoffeeFields().isEmpty());
    }

    @Test
    void testGetBrake() {
        assertEquals(0, airplane.getBrakes().getRedMarker());
        assertFalse(airplane.getBrakes().getBrakeFields().isEmpty());
    }

    @Test
    void testGetAltitude() {
        assertEquals(6000, airplane.getAltitude().getAltitudeValue());
        assertEquals(1, airplane.getAltitude().getRerollToken().getQuantity());
    }

    @Test
    void testFillTheCoffeeField() {
        airplane.fillCoffeeFields(3);
        assertTrue(airplane.getConcentration().getCoffeeFields().get(0).isFilled());
    }

    @Test
    void testDeployBrakes() {
        assertAll(
                () -> {
                    airplane.deployBrakes(3);
                    assertEquals(0, airplane.getBrakes().getRedMarker());
                },
                () -> {
                    airplane.deployBrakes(4);
                    assertEquals(0, airplane.getBrakes().getRedMarker());
                },
                () -> {
                    airplane.deployBrakes(2);
                    assertEquals(2, airplane.getBrakes().getRedMarker());
                },
                () -> {
                    airplane.deployBrakes(4);
                    assertEquals(4, airplane.getBrakes().getRedMarker());
                },
                () -> {
                    airplane.deployBrakes(6);
                    assertEquals(6, airplane.getBrakes().getRedMarker());
                },
                () -> {
                    airplane.deployBrakes(6);
                    assertEquals(6, airplane.getBrakes().getRedMarker());
                });
    }

    @Test
    void testDeployFlaps() {
        assertAll(
                () -> {
                    airplane.deployFlaps(3, 2);
                    assertEquals(8, airplane.getEngine().getOrangeMarker());
                },
                () -> {
                    airplane.deployFlaps(3, 1);
                    assertEquals(8, airplane.getEngine().getOrangeMarker());
                },
                () -> {
                    airplane.deployFlaps(2, 1);
                    assertEquals(9, airplane.getEngine().getOrangeMarker());
                },
                () -> {
                    airplane.deployFlaps(2, 2);
                    assertEquals(10, airplane.getEngine().getOrangeMarker());
                },
                () -> {
                    airplane.deployFlaps(4, 3);
                    assertEquals(11, airplane.getEngine().getOrangeMarker());
                },
                () -> {
                    airplane.deployFlaps(5, 4);
                    assertEquals(12, airplane.getEngine().getOrangeMarker());
                },
                () -> {
                    airplane.deployFlaps(5, 4);
                    assertEquals(12, airplane.getEngine().getOrangeMarker());
                });
    }

    @Test
    void testDeployLandingGears() {
        assertAll(
                () -> {
                    airplane.deployLandingGear(3, 2);
                    assertEquals(5, airplane.getEngine().getBlueMarker());
                },
                () -> {
                    airplane.deployLandingGear(3, 1);
                    assertEquals(5, airplane.getEngine().getBlueMarker());
                },
                () -> {
                    airplane.deployLandingGear(2, 1);
                    assertEquals(6, airplane.getEngine().getBlueMarker());
                },
                () -> {
                    airplane.deployLandingGear(5, 3);
                    assertEquals(7, airplane.getEngine().getBlueMarker());
                },
                () -> {
                    airplane.deployLandingGear(5, 3);
                    assertEquals(7, airplane.getEngine().getBlueMarker());
                });
    }

}
