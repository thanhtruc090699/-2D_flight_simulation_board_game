package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Radio;
import com.mygdx.skyteam.logic.Airplane;
import com.mygdx.skyteam.logic.Field;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class RadioTest {

    private Airplane airplane;
    private Radio radioCopilot;
    private Radio radioPilot;

    @BeforeEach
    void setUp() {

        airplane = new Airplane();
        radioPilot = new Radio(1);
        radioCopilot = new Radio(2);

    }

    @Test
    void testUseRadioCopilot() {
        assertAll(
                () -> {
                    assertFalse(radioCopilot.getRadioFields().get(0).isFilled());
                    assertFalse(radioCopilot.getRadioFields().get(1).isFilled());
                    radioCopilot.useRadio(airplane.getEngine().getCurrentPosition(), 3, 0, airplane.getPlanesOnTrack());
                    assertTrue(radioCopilot.getRadioFields().get(0).isFilled());
                },
                () -> {
                    radioCopilot.useRadio(airplane.getEngine().getCurrentPosition(), 1, 1, airplane.getPlanesOnTrack());
                    assertTrue(radioCopilot.getRadioFields().get(1).isFilled());
                },
                () -> {
                    // already occupied
                    radioCopilot.useRadio(airplane.getEngine().getCurrentPosition(), 1, 1, airplane.getPlanesOnTrack());
                    assertTrue(radioCopilot.getRadioFields().get(1).isFilled());
                });

    }

    @Test
    void testUseRadioPilot() {
        assertAll(
                () -> {
                    assertFalse(radioPilot.getRadioFields().get(0).isFilled());
                    radioPilot.useRadio(airplane.getEngine().getCurrentPosition(), 5, 0, airplane.getPlanesOnTrack());
                    assertTrue(radioPilot.getRadioFields().get(0).isFilled());
                },
                () -> {
                    // Already occupied
                    radioPilot.useRadio(airplane.getEngine().getCurrentPosition(), 3, 0, airplane.getPlanesOnTrack());
                    assertTrue(radioPilot.getRadioFields().get(0).isFilled());
                });

    }

    @Test
    void testRemovePlane() {
        assertAll(
                () -> {
                    radioCopilot.removePlane(1, airplane.getPlanesOnTrack());
                    assertEquals(0, airplane.getPlanesOnTrack().get(1));
                },
                () -> {
                    radioPilot.removePlane(3, airplane.getPlanesOnTrack());
                    assertEquals(1, airplane.getPlanesOnTrack().get(3));
                },
                () -> {
                    radioPilot.removePlane(3, airplane.getPlanesOnTrack());
                    assertEquals(0, airplane.getPlanesOnTrack().get(3));
                });

    }

    @Test
    void testResetRadioCopilotfield() {
        radioCopilot.resetFields();
        assertFalse(radioCopilot.getRadioFields().get(0).isFilled());
        assertFalse(radioCopilot.getRadioFields().get(1).isFilled());
    }

    @Test
    void testResetRadioPilotfield() {
        radioPilot.resetFields();
        assertFalse(radioPilot.getRadioFields().get(0).isFilled());
    }

    @Test
    void testGetCopilotRadioFields() {
        radioCopilot.getRadioFields();
        assertFalse(radioCopilot.getRadioFields().get(0).isFilled());
        assertFalse(radioCopilot.getRadioFields().get(1).isFilled());
    }

    @Test

    void testGetPilotRadioFields() {
        radioPilot.getRadioFields();
        assertFalse(radioPilot.getRadioFields().get(0).isFilled());
    }

    @Test
    void testUseRadioFieldAlreadyFilled() {
        int currentPosition = 2;
        int diceValue = 3;
        int chosenField = 0;

        radioPilot = new Radio(1);
        Field field = radioPilot.getRadioFields().get(chosenField);
        field.setDiceValue(diceValue);

        ArrayList<Integer> planesOnTrack = new ArrayList<>();
        planesOnTrack.add(1);

        radioPilot.useRadio(currentPosition, diceValue, chosenField, planesOnTrack);
        assertTrue(field.isFilled(), "Field should be filled.");
    }

}
