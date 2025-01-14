package com.mygdx.tests;
import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mygdx.skyteam.logic.Airplane;
import com.mygdx.skyteam.logic.CoPilot;
import com.mygdx.skyteam.logic.Pilot;
import com.mygdx.skyteam.logic.GameLogic;
import com.mygdx.skyteam.logic.Round;

public class RoundTest {

    private Pilot pilot;
    private CoPilot coPilot;
    private Airplane airplane;
    private GameLogic gameLogic;
    private Round round;

    @BeforeEach
    public void setUp() {
        // Initialize the objects used in Round
        airplane = new Airplane();
        gameLogic = new GameLogic();

        // Assume Pilot and CoPilot constructors take the necessary parameters
        pilot = new Pilot("Pilot", airplane);
        coPilot = new CoPilot("CoPilot", airplane);

        // Create a Round instance
        round = new Round(pilot, coPilot, airplane, gameLogic);
    }

    @Test
    public void testPlayRound() {
        // Simulate a round play
        round.collectPlayerInput(0, "engine", 1); // collect input for pilot
        round.playRound();

        assertTrue("Turns left should be 4", round.getTurnsLeft() == 4); // Since it's pilot's turn first
    }

    @Test
    public void testPlayTurn_pilotTurn() {
        // Setting input for Pilot turn
        round.collectPlayerInput(0, "engine", 1); // Assume these choices
        round.playTurn(0, "engine", 1);

        assertEquals("Pilot's turn should have completed", 0, round.getCurrentPlayerIndex());
    }

    @Test
    public void testPlayTurn_coPilotTurn() {
        // Setting input for CoPilot turn
        round.collectPlayerInput(0, "flap", 2); // Assume these choices
        round.switchPlayer();
        round.playTurn(1, "flap", 2);

        assertEquals("CoPilot's turn should have completed", 1, round.getCurrentPlayerIndex());
    }

    @Test
    public void testSwitchPlayer() {
        // Before switch, current player is pilot (index 0)
        assertEquals("Initial player should be pilot", 0, round.getCurrentPlayerIndex());

        // Switch player (pilot -> co-pilot)
        round.switchPlayer();
        assertEquals("After switch, current player should be co-pilot", 1, round.getCurrentPlayerIndex());

        // Switch again (co-pilot -> pilot)
        round.switchPlayer();
        assertEquals("After another switch, current player should be pilot", 0, round.getCurrentPlayerIndex());
    }

    @Test
    public void testCheckRoundConditions_pass() {
        // Simulate the conditions for a successful round (dice on required fields, etc.)
        airplane.getEngine().getPilotField().setPlacedDice(3);
        airplane.getAxis().getPilotAxisField().setPlacedDice(3);
        airplane.getEngine().getCoPilotField().setPlacedDice(3);
        airplane.getAxis().getCoPilotAxisField().setPlacedDice(3);
        airplane.getEngine().setPositionMoveSuccessful(true);
        assertTrue("Round should pass the conditions", round.checkRoundConditions());
    }

    @Test
    public void testCheckRoundConditions_fail() {
        // Simulate a failed condition (missing dice on required fields, etc.)
        // Modify conditions in the game (not shown in this simple test case)

        // Example: let's say pilot didn't place all necessary dice, which fails the condition
        assertFalse("Round should fail the conditions", round.checkRoundConditions());
    }

    @Test
    public void testCollectPlayerInput() {
        // Collect player input
        round.collectPlayerInput(2, "axis", 1);

        assertEquals("Dice index should be set correctly", 2, round.getCurrentDiceIndex());
        assertEquals("Player input should be set correctly", "axis", round.getCurrentPlayerInput());
        assertEquals("Field choice should be set correctly", 1, round.getCurrentFieldChoice());
    }

    @Test
    public void testCheckTurnConditions_fail() {
        // Simulate the case where one or both of the fields are filled improperly
        airplane.getEngine().getPilotField().setFilled(true); // Mock field filled
        airplane.getEngine().getCoPilotField().setFilled(true); // CoPilot field is not filled
        airplane.getEngine().setPositionMoveSuccessful(false);

        round.checkTurnConditions();

        assertTrue("Turn should fail due to incomplete fields", gameLogic.getGameOver());
    }
}
