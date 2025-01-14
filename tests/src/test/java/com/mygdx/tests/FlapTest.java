package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Flap;
import com.mygdx.skyteam.logic.Airplane;

import static org.junit.jupiter.api.Assertions.*;

class FlapTest {

    private Flap flap;
    private Airplane airplane;

    @BeforeEach
    void setUp() {
        flap = new Flap();
        airplane = new Airplane();
    }

    @Test
    void testInitialFlapsStatus() {
        assertAll(
                () -> assertFalse(flap.getFlapsFields().get(0).isFilled()),
                () -> assertFalse(flap.getFlapsFields().get(1).isFilled()),
                () -> assertFalse(flap.getFlapsFields().get(2).isFilled()),
                () -> assertFalse(flap.getFlapsFields().get(3).isFilled()));
    }

    @Test
    void testDeployFlap1() {
        flap.deployFlaps(1, airplane, 1);
        assertTrue(flap.getFlapsFields().get(0).isFilled());
    }

    @Test
    void testDeployFlap1AlreadyDeployed() {
        flap.deployFlaps(1, airplane, 1);
        flap.deployFlaps(1, airplane, 1); // Deploy again
        assertTrue(flap.getFlapsFields().get(0).isFilled());
    }

    @Test
    void testDeployFlap2WithoutFlap1() {
        flap.deployFlaps(2, airplane, 2);
        assertFalse(flap.getFlapsFields().get(1).isFilled());
    }

    @Test
    void testDeployFlap2WithFlap1() {
        flap.deployFlaps(1, airplane, 1);
        flap.deployFlaps(2, airplane, 2);
        assertTrue(flap.getFlapsFields().get(0).isFilled());
        assertTrue(flap.getFlapsFields().get(1).isFilled());
    }

    @Test
    void testDeployFlap3WithoutFlap2() {
        flap.deployFlaps(1, airplane, 1);
        flap.deployFlaps(4, airplane, 3);
        assertFalse(flap.getFlapsFields().get(2).isFilled());
    }

    @Test
    void testDeployFlap3WithFlap2() {
        flap.deployFlaps(1, airplane, 1);
        flap.deployFlaps(2, airplane, 2);
        flap.deployFlaps(4, airplane, 3);
        assertTrue(flap.getFlapsFields().get(2).isFilled());
    }

    @Test
    void testDeployFlap4WithoutFlap3() {
        flap.deployFlaps(1, airplane, 1);
        flap.deployFlaps(2, airplane, 2);
        flap.deployFlaps(5, airplane, 4);
        assertFalse(flap.getFlapsFields().get(3).isFilled());
    }

    @Test
    void testDeployFlap4WithFlap3() {
        flap.deployFlaps(1, airplane, 1);
        flap.deployFlaps(2, airplane, 2);
        flap.deployFlaps(4, airplane, 3);
        flap.deployFlaps(5, airplane, 4);
        assertTrue(flap.getFlapsFields().get(3).isFilled());
    }

    @Test
    void testDeployFlapShiftsOrangeMarker() {
        flap.deployFlaps(1, airplane, 1);

        assertEquals(9, airplane.getEngine().getOrangeMarker());
    }

    @Test
    void testDeployInvalidFlapValue() {

        flap.deployFlaps(5, airplane, 1); // Invalid flap number
        flap.deployFlaps(1, airplane, 4); // Invalid flap number

        assertAll(
                () -> assertFalse(flap.getFlapsFields().get(0).isFilled()),
                () -> assertFalse(flap.getFlapsFields().get(1).isFilled()),
                () -> assertFalse(flap.getFlapsFields().get(2).isFilled()),
                () -> assertFalse(flap.getFlapsFields().get(3).isFilled()));
    }

    @Test
    public void testCanPlaceFlap_validDiceAndFieldChoice() {
        flap.getFlapsFields().get(0).setFilled(true);
        assertTrue(flap.canPlaceFlap(2, 2)); // Field 2 has valid dice 4,5,6 and is filled
    }

    @Test
    public void testCanPlaceFlap_invalidDice() {
        flap.getFlapsFields().get(0).setFilled(true);
        assertFalse(flap.canPlaceFlap(4, 1)); // Field 1 has valid dice 1,2,3, not 4
    }

    @Test
    public void testCanPlaceFlap_unfilledPreviousField() {
        flap.getFlapsFields().get(0).setFilled(false); // Unfill field 1
        assertFalse(flap.canPlaceFlap(4, 2)); // Field 2 can't be filled before field 1
    }

    @Test
    void testCanPlaceFlapsForDice() {
        // Dice Value 1 or 2
        flap.getFlapsFields().get(0).setFilled(false);
        assertTrue(flap.canPlaceFlapsForDice(1,1), "Should return true when Flap 1 is not filled for dice 1.");
        flap.getFlapsFields().get(0).setFilled(true);
        assertFalse(flap.canPlaceFlapsForDice(1,1), "Should return false when Flap 1 is filled for dice 1.");

        // Dice Value 2 or 3
        flap.getFlapsFields().get(0).setFilled(true);
        flap.getFlapsFields().get(1).setFilled(false);
        assertTrue(flap.canPlaceFlapsForDice(2,2), "Should return true when Flap 1 is filled and Flap 2 is not filled for dice 2.");
        flap.getFlapsFields().get(1).setFilled(true);
        assertFalse(flap.canPlaceFlapsForDice(2,2), "Should return false when Flap 2 is already filled for dice 2.");

        // Dice Value 4 or 5
        flap.getFlapsFields().get(1).setFilled(true);
        flap.getFlapsFields().get(2).setFilled(false);
        assertTrue(flap.canPlaceFlapsForDice(4,3), "Should return true when Flap 1 and Flap 2 are filled and Flap 3 is not filled for dice 4.");
        flap.getFlapsFields().get(2).setFilled(true);
        assertFalse(flap.canPlaceFlapsForDice(4,3), "Should return false when Flap 3 is already filled for dice 4.");

        // Dice Value 5 or 6
        flap.getFlapsFields().get(2).setFilled(true);
        flap.getFlapsFields().get(3).setFilled(false);
        assertTrue(flap.canPlaceFlapsForDice(5,4), "Should return true when Flap 1, Flap 2, and Flap 3 are filled and Flap 4 is not filled for dice 5.");
        flap.getFlapsFields().get(3).setFilled(true);
        assertFalse(flap.canPlaceFlapsForDice(5,4), "Should return false when Flap 4 is already filled for dice 5.");
    }


}
