package com.mygdx.tests;

import com.mygdx.skyteam.logic.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.CoPilot;
import com.mygdx.skyteam.logic.Pilot;
import com.mygdx.skyteam.logic.Airplane;
import com.mygdx.skyteam.logic.Field;


import static org.junit.jupiter.api.Assertions.*;

class CoPilotTest {
    private CoPilot coPilot;
    private Pilot pilot;
    private Airplane airplane;

    @BeforeEach
    void setUp() {
        airplane = new Airplane();
        coPilot = new CoPilot("Copilot", airplane);
        pilot = new Pilot("Pilot", airplane);
    }
    @Test
    void testInvalidValues() {
        coPilot.placeDice(3,"enginer",1);
        assertNull(airplane.getEngine().getPilotField().getPlacedDice());
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
                    assertEquals(1,airplane.getEngine().getCurrentPosition());
                },
                ()->{
                    coPilot.placeDice(3,"ENGINE",0);
                    pilot.placeDice(4,"ENGINE",0);
                    assertEquals(3,airplane.getEngine().getCoPilotField().getPlacedDice());
                    assertEquals(4,airplane.getEngine().getPilotField().getPlacedDice());
                    assertEquals(7,airplane.getEngine().getSpeed());
                    assertEquals(1,airplane.getEngine().getCurrentPosition());
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

                ()->{
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
    void testPlaceDiceFlaps(){
        assertAll(
                ()->{ // wrong order

                    for(Field flapField : airplane.getFlaps().getFlapsFields()){
                        assertFalse(flapField.isFilled());
                    }


                    coPilot.placeDice(3,"flaps",2);
                    assertFalse(airplane.getFlaps().getFlapsFields().get(1).isFilled());
                    assertEquals(8,airplane.getEngine().getOrangeMarker());

                },
                ()-> { // correct order Flap 1 activated
                    Field flap1 = airplane.getFlaps().getFlapsFields().get(0);
                    assertFalse(flap1.isFilled());
                    coPilot.placeDice(2,"flaps",1);
                    assertTrue(flap1.isFilled());
                    assertEquals(10,airplane.getEngine().getOrangeMarker());

                },
                ()->{ // Flap 2 activated
                    Field flap2 = airplane.getFlaps().getFlapsFields().get(1);
                    assertFalse(flap2.isFilled());
                    coPilot.placeDice(3,"flaps",2);
                    assertTrue(flap2.isFilled());
                    assertEquals(10,airplane.getEngine().getOrangeMarker());
                },
                ()-> {
                    // Already occupied
                    Field flap2 = airplane.getFlaps().getFlapsFields().get(1);
                    assertTrue(flap2.isFilled());
                    coPilot.placeDice(3,"flaps",2);
                    assertTrue(flap2.isFilled());
                    assertEquals(10,airplane.getEngine().getOrangeMarker());
                },
                ()->{
                    // Wrong required value
                    Field flap3 = airplane.getFlaps().getFlapsFields().get(2);
                    assertFalse(flap3.isFilled());
                    coPilot.placeDice(3,"flaps",3);
                    assertFalse(flap3.isFilled());
                    assertEquals(10,airplane.getEngine().getOrangeMarker());


                },
                ()->{
                    // Flap 3 activated
                    Field flap3 = airplane.getFlaps().getFlapsFields().get(2);
                    coPilot.placeDice(4,"flaps",3);
                    assertTrue(flap3.isFilled());
                    assertEquals(11,airplane.getEngine().getOrangeMarker());

                },
                ()->{ //Flap 4 activated
                    Field flap4 = airplane.getFlaps().getFlapsFields().get(3);
                    coPilot.placeDice(6,"flaps",4);
                    assertTrue(flap4.isFilled());
                    assertEquals(12,airplane.getEngine().getOrangeMarker());

                }
            /*
                ()-> { //All Flaps are occupied
                    Field flap4 = airplane.getFlaps().getFlapsFields().get(3);
                    coPilot.placeDice(6,"flaps",5);
                    assertTrue(flap4.isFilled());
                    assertEquals(12,airplane.getEngine().getOrangeMarker());

                }

             */
        );

    }


    @Test
    void testPlaceDiceRadio(){
        assertFalse(coPilot.getRadio().getRadioFields().get(0).isFilled());
        assertFalse(coPilot.getRadio().getRadioFields().get(1).isFilled());
        coPilot.placeDice(3,"RADIO",1);
        assertTrue(coPilot.getRadio().getRadioFields().get(1).isFilled());

    }

    @Test
    void testPlaceDiceConcentration(){
        assertAll(
                ()->{

                    coPilot.placeDice(3,"Coffee",0);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(0).isFilled());
                    assertEquals(1,coPilot.getCoffeeToken().getQuantity());
                },
                ()->{
                    coPilot.placeDice(3,"Coffee",0);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(1).isFilled());
                    assertEquals(2,coPilot.getCoffeeToken().getQuantity());
                },
                ()->{
                    coPilot.placeDice(3,"Coffee",2);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(2).isFilled());
                    assertEquals(3,coPilot.getCoffeeToken().getQuantity());
                },
                ()-> {
                    // when you already had 3 coffee but too greedy and want more
                    coPilot.placeDice(3,"Coffee",1);
                    assertTrue(airplane.getConcentration().getCoffeeFields().get(1).isFilled());
                    assertEquals(3,coPilot.getCoffeeToken().getQuantity());
                }
        );
    }
    @Test
    void testHasDicesOnRequiredFields(){
        assertAll(
                ()->{
                    coPilot.placeDice(3,"Axis",0);
                    assertFalse(coPilot.hasDicesOnRequiredFields());

                },
                ()-> {
                    coPilot.placeDice(3,"Engine",0);
                    assertTrue(coPilot.hasDicesOnRequiredFields());
                }
        );
    }
    @Test
    void testGetRadio(){
        assertFalse(coPilot.getRadio().getRadioFields().get(0).isFilled());
        assertFalse(coPilot.getRadio().getRadioFields().get(1).isFilled());
    }

}
