package com.mygdx.tests;

import com.mygdx.skyteam.logic.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Pilot;
import com.mygdx.skyteam.logic.CoPilot;
import com.mygdx.skyteam.logic.Airplane;

import static org.junit.jupiter.api.Assertions.*;

class PilotTest {
    private Pilot pilot;
    private CoPilot coPilot;
    private Airplane airplane;

    @BeforeEach
    void setUp() {
        airplane = new Airplane();
        pilot = new Pilot("Pilot", airplane);
        coPilot = new CoPilot("CoPilot", airplane);
    }
    @Test
    void testInvalidValues() {
        pilot.placeDice(3,"enginer",1);
        assertNull(airplane.getEngine().getCoPilotField().getPlacedDice());
    }
    @Test
    void testPlaceDiceEngine(){
        coPilot.placeDice(3,"ENGINE",0);
        pilot.placeDice(3,"ENGINE",0);
        assertEquals(3,airplane.getEngine().getCoPilotField().getPlacedDice());
        assertEquals(3,airplane.getEngine().getPilotField().getPlacedDice());
        assertEquals(6,airplane.getEngine().getSpeed());
    }
    @Test
    void testPlaceDiceEngineWhenCrashHappen(){
        assertAll(
                ()->{
                    coPilot.placeDice(3,"ENGINE",0);
                    pilot.placeDice(4,"ENGINE",0);
                    assertEquals(3,airplane.getEngine().getCoPilotField().getPlacedDice());
                    assertEquals(4,airplane.getEngine().getPilotField().getPlacedDice());
                    assertEquals(7,airplane.getEngine().getSpeed());
                },
                ()->{
                    airplane.getEngine().resetFields();
                    coPilot.placeDice(6,"ENGINE",0);
                    pilot.placeDice(6,"ENGINE",0);
                    assertEquals(6,airplane.getEngine().getCoPilotField().getPlacedDice());
                    assertEquals(6,airplane.getEngine().getPilotField().getPlacedDice());
                    assertEquals(12,airplane.getEngine().getSpeed());
                }
        );
    }


    @Test
    void testPlaceDiceAxis(){
        coPilot.placeDice(3,"AXIS",0);
        pilot.placeDice(4,"AXIS",0);
        assertEquals(3,airplane.getAxis().getCoPilotAxisField().getPlacedDice());
        assertEquals(4,airplane.getAxis().getPilotAxisField().getPlacedDice());
        assertEquals(2,airplane.getAxis().getCurrentTilt());
    }

    @Test
    void testPlaceDiceAxisPass90Degree(){
        assertAll(
                ()->{
                    coPilot.placeDice(3,"AXIS",0);
                    pilot.placeDice(4,"AXIS",0);
                    assertEquals(3,airplane.getAxis().getCoPilotAxisField().getPlacedDice());
                    assertEquals(4,airplane.getAxis().getPilotAxisField().getPlacedDice());
                    assertEquals(2,airplane.getAxis().getCurrentTilt());
                },
                ()->{
                    airplane.getAxis().resetFields();
                    coPilot.placeDice(3,"AXIS",0);
                    pilot.placeDice(4,"AXIS",0);
                    assertEquals(3,airplane.getAxis().getCoPilotAxisField().getPlacedDice());
                    assertEquals(4,airplane.getAxis().getPilotAxisField().getPlacedDice());
                    assertEquals(1,airplane.getAxis().getCurrentTilt());

                },

                ()->{ //Game over
                    airplane.getAxis().resetFields();
                    coPilot.placeDice(3,"AXIS",0);
                    pilot.placeDice(4,"AXIS",0);
                    assertEquals(3,airplane.getAxis().getCoPilotAxisField().getPlacedDice());
                    assertEquals(4,airplane.getAxis().getPilotAxisField().getPlacedDice());
                    assertEquals(0,airplane.getAxis().getCurrentTilt());
                }

        );
    }
    @Test
    void testPlaceDiceLandingGear(){
        assertAll(
                ()->{
                    // in Landing gear, the order is not important like in Flaps

                    for(Field landingField : airplane.getLandingGears().getLandingGearFields()){
                        assertFalse(landingField.isFilled());
                    }
                    // gear 2 activated
                    pilot.placeDice(3,"landing gears",2);
                    assertTrue(airplane.getLandingGears().getLandingGearFields().get(1).isFilled());
                    assertEquals(5,airplane.getEngine().getBlueMarker());

                },

                ()->{
                    // wrong input for gear 1
                    pilot.placeDice(3,"landing gears",1);
                    assertFalse(airplane.getLandingGears().getLandingGearFields().get(0).isFilled());
                    assertEquals(5,airplane.getEngine().getBlueMarker());
                },
                ()-> {
                    // correct input for gear 1
                    pilot.placeDice(2,"landing gears",1);
                    assertTrue(airplane.getLandingGears().getLandingGearFields().get(0).isFilled());
                    assertEquals(6,airplane.getEngine().getBlueMarker());

                },
                ()->{
                    // gear already occupied
                    pilot.placeDice(2,"landing gears",1);
                    assertTrue(airplane.getLandingGears().getLandingGearFields().get(0).isFilled());
                    assertEquals(6,airplane.getEngine().getBlueMarker());


                },
                ()-> {
                    //all the gear occupied
                    pilot.placeDice(5,"landing gears",3);
                    assertTrue(airplane.getLandingGears().getLandingGearFields().get(2).isFilled());
                    assertEquals(7,airplane.getEngine().getBlueMarker());

                },
                ()-> {
                    // all occupied so no more
                    pilot.placeDice(5,"landing gears",3);
                    assertTrue(airplane.getLandingGears().getLandingGearFields().get(2).isFilled());
                }
        );

    }

    @Test

    void testPlaceDiceRadio(){
        assertFalse(pilot.getRadio().getRadioFields().get(0).isFilled());
        pilot.placeDice(3,"RADIO",1);
        assertTrue(pilot.getRadio().getRadioFields().get(0).isFilled());
    }

    @Test

    void testPlaceDiceBrakes(){
        assertAll(
                ()->{
                    // Wrong order
                    pilot.placeDice(4,"BRAKE",2);
                    assertFalse(airplane.getBrakes().getBrakeFields().get(1).isFilled());
                },
                ()->{
                    // Correct order Brake 1 activated
                    pilot.placeDice(2,"BRAKE",1);
                    assertTrue(airplane.getBrakes().getBrakeFields().get(0).isFilled());
                },
                ()->{
                    // Break 1 already occupied
                    pilot.placeDice(2,"BRAKE",1);
                    assertTrue(airplane.getBrakes().getBrakeFields().get(0).isFilled());
                },
                ()->{
                    pilot.placeDice(4,"BRAKE",2);
                    assertTrue(airplane.getBrakes().getBrakeFields().get(1).isFilled());
                },
                ()->{
                    pilot.placeDice(6,"BRAKE",3);
                    assertTrue(airplane.getBrakes().getBrakeFields().get(2).isFilled());
                },
                ()->{
                    // All Brake levels have been activated
                    pilot.placeDice(6,"BRAKE",3);
                    assertTrue(airplane.getBrakes().getBrakeFields().get(2).isFilled());
                }
        );
    }

    @Test
    void testPlaceDiceConcentration(){
        assertAll(
                ()->{

                    pilot.placeDice(3,"Coffee",0);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(0).isFilled());
                    assertEquals(1,pilot.getCoffeeToken().getQuantity());
                },
                ()->{
                    pilot.placeDice(4,"Coffee",0);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(1).isFilled());
                    assertEquals(2,pilot.getCoffeeToken().getQuantity());
                },
                ()->{
                    pilot.placeDice(3,"Coffee",2);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(2).isFilled());
                    assertEquals(3,pilot.getCoffeeToken().getQuantity());
                },
                ()-> {
                    // when you already had 3 coffee but too greedy and want more
                    pilot.placeDice(3,"Coffee",1);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(1).isFilled());
                    assertEquals(3,pilot.getCoffeeToken().getQuantity());
                }
        );
    }

    @Test
    void testDicesOnRequiredFields(){
        assertAll(
                ()->{
                    pilot.placeDice(3,"Axis",0);
                    assertFalse(pilot.hasDicesOnRequiredFields());
                },
                ()->{
                    pilot.placeDice(3,"engine",0);
                    assertTrue(pilot.hasDicesOnRequiredFields());
                }
        );
    }
    @Test
    void testGetRaidoPilot() {
        assertFalse(pilot.getRadio().getRadioFields().get(0).isFilled());
    }
}
