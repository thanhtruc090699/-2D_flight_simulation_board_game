package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.LandingGear;
import com.mygdx.skyteam.logic.Airplane;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class LandingGearTest {
    private LandingGear landingGear;
    private Airplane airplane;

    @BeforeEach
    void setUp() {
        landingGear = new LandingGear();
        airplane = new Airplane();
    }

    @Test
    void testLandingGearInitialState() {
        assertEquals(3, landingGear.getLandingGearFields().size());

        assertAll(
                () -> assertFalse(landingGear.getLandingGearFields().get(0).isFilled()),
                () -> assertFalse(landingGear.getLandingGearFields().get(1).isFilled()),
                () -> assertFalse(landingGear.getLandingGearFields().get(2).isFilled()));

        assertTrue(landingGear.getLandingGearFields().get(0).getValidDiceValues().containsAll(Arrays.asList(1, 2)),
                "Landing Gear 1 should accept dice values 1 and 2.");
    }

    @Test
    void testDeployLandingGear1() {
        landingGear.deployLandingGear(1, airplane, 1);
        assertTrue(landingGear.getLandingGearFields().get(0).isFilled());
    }

    @Test
    void testDeployLandingGear2WithoutLandingGear1() {
        landingGear.deployLandingGear(3, airplane, 2);
        assertTrue(landingGear.getLandingGearFields().get(1).isFilled());
    }

    @Test
    void testDeployLandingGear2WithLandingGear1() {
        landingGear.deployLandingGear(1, airplane, 1);
        landingGear.deployLandingGear(3, airplane, 2);
        assertTrue(landingGear.getLandingGearFields().get(1).isFilled());
    }

    @Test
    void testDeployLandingGear3WithoutLandingGear2() {
        landingGear.deployLandingGear(1, airplane, 1);
        landingGear.deployLandingGear(5, airplane, 3);
        assertTrue(landingGear.getLandingGearFields().get(2).isFilled());
    }

    @Test
    void testDeployLandingGear3WithLandingGear2() {
        landingGear.deployLandingGear(1, airplane, 1);
        landingGear.deployLandingGear(3, airplane, 2);
        landingGear.deployLandingGear(5, airplane, 3);
        assertTrue(landingGear.getLandingGearFields().get(2).isFilled());
    }

    @Test
    void testDeployLandingGearInvalidDiceValue() {
        landingGear.deployLandingGear(5, airplane, 1);

        assertFalse(landingGear.getLandingGearFields().get(0).isFilled(),
                "Landing Gear 1 should not be deployed with an invalid dice value.");
    }

    @Test
    void testDeployLandingGearShiftsBlueMarker() {
        landingGear.deployLandingGear(1, airplane, 1);

        assertEquals(5, airplane.getEngine().getBlueMarker());
    }

    @Test
    void testCanPlaceLandingGear_validDiceAndFieldChoice() {
        landingGear.getLandingGearFields().get(0).setFilled(true);
        assertTrue(landingGear.canPlaceLandingGear(4, 2));
    }

    @Test
    public void testCanPlaceLandingGear_invalidDice() {
        landingGear.getLandingGearFields().get(0).setFilled(true);
        assertFalse(landingGear.canPlaceLandingGear(4, 1));
    }

    @Test
    public void testCanPlaceLandingGear_unfilledPreviousField() {
        landingGear.getLandingGearFields().get(0).setFilled(false);
        assertFalse(landingGear.canPlaceLandingGear(4, 2));
    }

}
