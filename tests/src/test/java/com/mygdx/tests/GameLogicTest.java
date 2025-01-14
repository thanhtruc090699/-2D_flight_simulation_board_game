package com.mygdx.tests;

import com.mygdx.skyteam.logic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {

    private GameLogic game;
    private Airplane airplane;

    @BeforeEach
    public void setUp() {
        game = new GameLogic();
        airplane = new Airplane();
        setInitialAirplaneConditions();
    }

    private void setInitialAirplaneConditions() {
        // Initialize all airplane components
        for (Field field : game.getAirplane().getLandingGears().getLandingGearFields()) {
            field.setFilled(false);
        }
        for (Field field : game.getAirplane().getFlaps().getFlapsFields()) {
            field.setFilled(false);
        }
        game.getAirplane().getEngine().resetFields();
        game.getAirplane().getAxis().resetFields();
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(game, "Game should be initialized");
        assertEquals(1, game.getCurrentRoundNumber(), "Game should start at round 1");
        assertFalse(game.getGameOver(), "Game should not be over initially");
    }

    @Test
    public void testStartGame() {
        game.startGame();
        assertNotNull(game.getPilot(), "Pilot should be initialized");
        assertNotNull(game.getCoPilot(), "CoPilot should be initialized");
        assertNotNull(game.getRound(), "Round should be initialized");
    }

    @Test
    public void testNextRound() {
        game.startGame();
        airplane.getEngine().getPilotField().setPlacedDice(3);
        airplane.getAxis().getPilotAxisField().setPlacedDice(3);
        airplane.getEngine().getCoPilotField().setPlacedDice(3);
        airplane.getAxis().getCoPilotAxisField().setPlacedDice(3);
        airplane.getEngine().setPositionMoveSuccessful(true);
        game.nextRound();
        assertEquals(2, game.getCurrentRoundNumber(), "Round number should increase");
    }

    @Test
    public void testNextRoundEndGame() {
        game.startGame();

        // Simulate reaching the final round
        for (int i = 0; i < 6; i++) {
            airplane.getEngine().getPilotField().setPlacedDice(3);
            airplane.getAxis().getPilotAxisField().setPlacedDice(3);
            airplane.getEngine().getCoPilotField().setPlacedDice(3);
            airplane.getAxis().getCoPilotAxisField().setPlacedDice(3);
            airplane.getEngine().setPositionMoveSuccessful(true);
            game.nextRound();
        }

        assertEquals(7, game.getCurrentRoundNumber(), "Round number should be 7 after final round");
        assertFalse(game.getGameOver(), "Game should not be over yet");

        game.nextRound();
        assertTrue(game.getGameOver(), "Game should end after the 7th round");
    }

    @Test
    public void testEndGameWin() {
        game.startGame();

        // Set winning conditions
        for (Field field : game.getAirplane().getLandingGears().getLandingGearFields()) {
            field.setFilled(true);
        }
        for (Field field : game.getAirplane().getFlaps().getFlapsFields()) {
            field.setFilled(true);
        }
        game.getAirplane().getEngine().setCurrentPosition(6);
        game.getAirplane().getAxis().setTilt(3);
        game.getAirplane().getEngine().setSpeed(2);
        game.getAirplane().getBrakes().setRedMarker(3);

        game.endGame();
        assertTrue(game.hasWon(), "Game should be won");
    }

    @Test
    public void testEndGameLose() {
        game.startGame();

        // Set losing conditions
        game.getAirplane().getEngine().setCurrentPosition(3);

        game.endGame();
        assertTrue(game.getGameOver(), "Game should be over");
        assertFalse(game.hasWon(), "Game should not be won");
    }

    @Test
    public void testCheckWinningConditionsFailure() {
        game.startGame();

        // Leave one Landing Gear field unfilled
        game.getAirplane().getLandingGears().getLandingGearFields().get(0).setFilled(false);

        boolean won = game.checkWinningConditions();
        assertFalse(won, "Game should fail the winning conditions");
    }

    @Test
    public void testGetters() {
        game.startGame();
        assertNotNull(game.getPilot(), "Pilot should be retrievable");
        assertNotNull(game.getCoPilot(), "CoPilot should be retrievable");
        assertNotNull(game.getAirplane(), "Airplane should be retrievable");
        assertNotNull(game.getRound(), "Current round should be retrievable");
        assertEquals(1, game.getCurrentRoundNumber(), "Current round number should be correct");
    }

    @Test
    public void testGetAllFieldsForPilot() {
        game.startGame();
        List<Field> pilotFields = game.getAllFieldsForPilot();
        assertNotNull(pilotFields, "Pilot fields should not be null");
        assertFalse(pilotFields.isEmpty(), "Pilot fields should not be empty");
    }

    @Test
    public void testGetAllFieldsForCoPilot() {
        game.startGame();
        List<Field> coPilotFields = game.getAllFieldsForCoPilot();
        assertNotNull(coPilotFields, "CoPilot fields should not be null");
        assertFalse(coPilotFields.isEmpty(), "CoPilot fields should not be empty");
    }
}
