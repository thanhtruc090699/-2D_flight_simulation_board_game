package com.mygdx.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.Player;
import com.mygdx.skyteam.logic.Token;
import com.mygdx.skyteam.logic.Airplane;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
   private Player player;
   private Airplane airplane;
   private Token rerollToken;

   @BeforeEach
    void setUp(){
       airplane = new Airplane();
       rerollToken = airplane.getAltitude().getRerollToken();
       player = new Player("Davni","pilot",airplane) {
           @Override
           public void placeDice(int diceValue, String playerInput, int placeHolder) {

           }
       };
   }
   @Test
    void testDisplayUnassignedDice(){
       player.displayUnassignedDice();
        assertNotNull(player.getUnassignedDice());
   }

   @Test
    void testHasRerollToken(){
       player.hasRerollToken();
       assertEquals(1,rerollToken.getQuantity());
   }

   @Test
   void testUseRerollToken(){
       assertAll(
               ()->{ //Test Case with hardcode testing with input = 1

                   //Create a arraylist to save the value of the dice before and after being rerolled to compare

                   ArrayList<Integer> diceValuesBevorUndNachReroll = new ArrayList<Integer>();

                   // Add the value of the first dice before being rerolled to the list
                   diceValuesBevorUndNachReroll.add(0,player.getDices().get(0).getDiceValue());

                   // Make sure that the reroll token default quantity is 1
                   assertEquals(1,rerollToken.getQuantity());

                   player.useRerollToken();

                   // Add the value of the first dice after being rerolled to the list
                   diceValuesBevorUndNachReroll.add(1,player.getDices().get(0).getDiceValue());

                   // After being used, there would be no token left, the quantity equals 0
                   assertEquals(0,rerollToken.getQuantity());

                   //The value before and after rerolling should be diff
                   assertTrue(diceValuesBevorUndNachReroll.get(0).intValue()!=diceValuesBevorUndNachReroll.get(1).intValue());
               },
               ()->{
                   // when there is no reroll token left to use
                   assertEquals(0,rerollToken.getQuantity());
                   player.useRerollToken();
                   // Receive the message "No more tokens to reroll."
                   assertEquals(0,rerollToken.getQuantity());
               }
       );
   }

   /*
   @Test
    void testUseRerollToken(){
       assertAll(

               ()->{
                   //set up choose done to refuse to use reroll Tokens

                   String simulatedInput = "4";
                   InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
                   System.setIn(in);
                   assertTrue(rerollToken.getQuantity()==1);

                   player.useRerollToken();

                   assertEquals(0,rerollToken.getQuantity());
                   assertNotNull(player.getUnassignedDice());
               },
               ()->{
                   //set up choose 4 dices to reroll
                   String simulatedInput = "1";
                   InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
                   System.setIn(in);
                   player.useRerollToken();

                   //expected that after using reroll Token, the remain Token is 0
                   assertEquals(0,rerollToken.getQuantity());
                   assertNotNull(player.getUnassignedDice());

               }/*,

               ()->{ //no more reroll Tokens left to use
                   //set up choose 2 dices to reroll
                   String simulatedInput = "2";
                   InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
                   System.setIn(in);
                   player.useRerollToken();

                   assertEquals(0,rerollToken.getQuantity());
                   assertNotNull(player.getUnassignedDice());
               }

       );
   }

    */

   @Test
    void testResetDiceAssignments(){

       player.resetDiceAssignments();
       for(int i=0;i<player.getUnassignedDice().size();i++){

           assertTrue(!player.getUnassignedDice().get(i).isAssigned(),"to ensure each Dice has `isAssigned == false`.");
       }
   }
   @Test
    void testRerollDice(){

       // List of dice values before being rerolled
       ArrayList<Integer> initialDicesValue = new ArrayList<Integer>();
       for(int i = 0;i<player.getDices().size();i++){
           initialDicesValue.add(player.getDices().get(i).getDiceValue());
       }
       player.displayUnassignedDice();
        int common = 0;

       player.rerollDice();

       // List of dice values after being rerolled
       ArrayList<Integer> afterBeingrerolledDicesValue = new ArrayList<Integer>();
       for(int i = 0;i<player.getDices().size();i++){
           afterBeingrerolledDicesValue.add(player.getDices().get(i).getDiceValue());
       }
       player.displayUnassignedDice();

       //Check if dice values have been rerolled or not
        for(int i=0;i<initialDicesValue.size();i++){
            if(initialDicesValue.get(i).equals(afterBeingrerolledDicesValue.get(i))){
                common++;
            }
        }
        assertTrue(common<4,"when common less than 4, it means dice list has been rerolled");

   }
   @Test
    void testGetUnassignedDice(){
       for(int i=0;i<player.getUnassignedDice().size();i++){

           assertTrue(!player.getUnassignedDice().get(i).isAssigned(),"to ensure each Dice has `isAssigned == false`.");
       }
       assertNotNull(player.getUnassignedDice());
   }

   @Test
    void testGetDices(){
       // Dice list never null
       assertNotNull(player.getDices());

       //check if the value of the dice is within the range from 1 to 6
       for(int i=0;i<player.getDices().size();i++){
           assertTrue(player.getDices().get(i).getDiceValue()>=1 && player.getDices().get(i).getDiceValue()<=6);
       }
   }

   @Test
    void testGetCoffeeToken(){
       assertEquals(0,player.getCoffeeToken().getQuantity());
       assertEquals("Coffee",player.getCoffeeToken().getType());
   }

   @Test
    void testGetName(){
       assertEquals("Davni",player.getName());
    }
}
