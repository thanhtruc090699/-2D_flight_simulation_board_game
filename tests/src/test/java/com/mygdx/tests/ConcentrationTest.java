package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Concentration;
import com.mygdx.skyteam.logic.Field;

import static org.junit.jupiter.api.Assertions.*;

public class ConcentrationTest {

    Concentration concentration;

    @BeforeEach
    void setUp(){
        concentration = new Concentration();
    }

    @Test
    void testInitialization(){
        assertEquals(3, concentration.getCoffeeFields().size());

        for(Field field: concentration.getCoffeeFields()){
            assertFalse(field.isFilled(), field.getName() + " should not be filled initially.");

        }
    }

    @Test
    void testFillCoffeeFieldsValidValue() {
        
        concentration.fillCoffeeFields(4);
        
       
        Field firstField = concentration.getCoffeeFields().get(0);
        assertTrue(firstField.isFilled(), firstField.getName() + " should be filled.");
        assertEquals(4, firstField.getPlacedDice(), firstField.getName() + " should have dice value 4.");
    }
    @Test
    void testFillCoffeeFieldsInvalidValue() {
        
        concentration.fillCoffeeFields(7);
        
       
        for (Field field : concentration.getCoffeeFields()) {
            assertFalse(field.isFilled(), field.getName() + " should not be filled.");
        }
    }
    
    @Test
    void testResetCoffeeFields(){
        concentration.fillCoffeeFields(6);
        Field firstField = concentration.getCoffeeFields().get(0);
        assertTrue(firstField.isFilled());

        concentration.resetCoffeeField(0);
        assertFalse(firstField.isFilled());
    }

    @Test
    void testResetCoffeeFieldInvalidIndex(){
        assertThrows(IndexOutOfBoundsException.class, () -> {
        concentration.resetCoffeeField(10);
        });
    }

    @Test 
    void testFillingWhenAllFieldsAreFilled(){
        concentration.fillCoffeeFields(6);
        concentration.fillCoffeeFields(4);
        concentration.fillCoffeeFields(1);
        concentration.fillCoffeeFields(2);

        for(Field field : concentration.getCoffeeFields()){
            assertTrue(field.isFilled());
        }
    }
}
