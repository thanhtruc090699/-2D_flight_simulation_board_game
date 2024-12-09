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
    void testDeployFlapShiftsOrangeMarker(){
        flap.deployFlaps(1, airplane, 1);

        assertEquals(9, airplane.getEngine().getOrangeMarker());
    }

    @Test
    void testDeployInvalidFlapValue() {
        
        flap.deployFlaps(5, airplane, 1);  // Invalid flap number
        flap.deployFlaps(1, airplane, 4);  // Invalid flap number

        assertAll(
            () -> assertFalse(flap.getFlapsFields().get(0).isFilled()),
            () -> assertFalse(flap.getFlapsFields().get(1).isFilled()),
            () -> assertFalse(flap.getFlapsFields().get(2).isFilled()),
            () -> assertFalse(flap.getFlapsFields().get(3).isFilled())
        );
    }


}