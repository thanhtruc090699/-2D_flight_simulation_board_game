package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Altitude;
import com.mygdx.skyteam.logic.Token;

import static org.junit.jupiter.api.Assertions.*;

class AltitudeTest {

    private Altitude altitude;
    private Token rerollToken;
    
    @BeforeEach
    void setUp() {
        altitude = new Altitude();
    }

    @Test
    void testInitialValues() {
        assertEquals(6000, altitude.getAltitudeValue(), "Initial altitude should be 6000.");
        assertNotNull(altitude.getRerollToken(), "Reroll token should not be null.");
        assertEquals(1, altitude.getRerollToken().getQuantity(), "Initial reroll token quantity should be 1.");
    }

    @Test
    void testAdjustAltitudeRound2() {
        altitude.adjustAltitude(2);
        assertEquals(5000, altitude.getAltitudeValue(), "Altitude should decrease by 1000.");
        assertEquals(1, altitude.getRerollToken().getQuantity(), "Reroll token quantity should remain unchanged before round 5.");
    }

    @Test
    void testAdjustAltitudeRound3() {
        altitude.adjustAltitude(3);
        assertEquals(4000, altitude.getAltitudeValue(), "Altitude should decrease by 1000.");
        assertEquals(1, altitude.getRerollToken().getQuantity(), "Reroll token quantity should remain unchanged before round 5.");
    }

    @Test
    void testAdjustAltitudeRound4() {
        altitude.adjustAltitude(4);
        assertEquals(3000, altitude.getAltitudeValue(), "Altitude should decrease by 1000.");
        assertEquals(1, altitude.getRerollToken().getQuantity(), "Reroll token quantity should remain unchanged before round 5.");
    }

    @Test
    void testAdjustAltitudeOnRound5() {
        altitude.adjustAltitude(5);
        assertEquals(2000, altitude.getAltitudeValue(), "Altitude should decrease by 1000 on round 5.");
        assertEquals(2, altitude.getRerollToken().getQuantity(), "Reroll token quantity should increase by 1 on round 5.");
    }

    @Test
    void testAdjustAltitudeRound6() {
        altitude.adjustAltitude(6);
        assertEquals(1000, altitude.getAltitudeValue(), "Altitude should decrease by 1000.");
    }

    @Test
    void testAdjustAltitudeRound7() {
        altitude.adjustAltitude(7);
        assertEquals(0, altitude.getAltitudeValue(), "Altitude should decrease by 1000.");
    }

    @Test
    void testSetAltitudeValidInput() {
        assertAll("Valid inputs for setting altitude",
                () -> {
                    altitude.setAltitude(3000);
                    assertEquals(3000, altitude.getAltitudeValue(), "Altitude should be updated to 3000.");
                },
                () -> {
                    altitude.setAltitude(0);
                    assertEquals(0, altitude.getAltitudeValue(), "Altitude should be updated to 0.");
                },
                () -> {
                    altitude.setAltitude(5000);
                    assertEquals(5000, altitude.getAltitudeValue(), "Altitude should be updated to 5000.");
                }
        );
    }

    @Test
    void testSetAltitudeInvalidInput() {
        assertAll(
                () -> {assertThrows(IllegalArgumentException.class, () -> altitude.setAltitude(7000));},
                () -> {assertThrows(IllegalArgumentException.class, () -> altitude.setAltitude(-1));}
        );

    }


    @Test
    void testGetAltitudeValue() {
        assertEquals(6000, altitude.getAltitudeValue(), "Initial altitude should be 6000.");
        altitude.setAltitude(3000);
        assertEquals(3000, altitude.getAltitudeValue(), "Altitude should return the updated value.");
    }

    @Test
    void testGetRerollTokenInitial() {
        Token token = altitude.getRerollToken();
        assertNotNull(token, "Reroll token should not be null.");
        assertEquals("reroll", token.getType(), "Reroll token type should be 'reroll'.");
        assertEquals(1, token.getQuantity(), "Initial reroll token quantity should be 1.");
    }
}