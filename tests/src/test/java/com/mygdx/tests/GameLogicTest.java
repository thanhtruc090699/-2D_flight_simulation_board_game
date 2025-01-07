package com.mygdx.tests;

import com.mygdx.skyteam.logic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mygdx.skyteam.logic.GameLogic;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {

    private GameLogic game;

    @BeforeEach
    public void setUp() {
        // Initialize the Game
        game = new GameLogic();

        // Manually set the airplane fields for testing
        setInitialAirplaneConditions();
    }

    private void setInitialAirplaneConditions() {
        // Ensure all Landing Gear fields are initialized and accessible
        for (Field field : game.getAirplane().getLandingGears().getLandingGearFields()) {
            assertNotNull(field, "Landing Gear fields should not be null");
            field.setFilled(false);
        }

        // Ensure all Flaps fields are initialized and accessible
        for (Field field : game.getAirplane().getFlaps().getFlapsFields()) {
            assertNotNull(field, "Flap fields should not be null");
            field.setFilled(false);
        }

        // Reset engine and axis conditions
        game.getAirplane().getEngine().resetFields();
        game.getAirplane().getAxis().resetFields();
    }

    @Test
    public void testGameInitialization() {
        // Verify that the game initializes correctly
        assertNotNull(game, "Game should be initialized");
        assertEquals(1, game.getCurrentRoundNumber(), "Game should start at round 1");
        assertFalse(game.getGameOver(), "Game should not be over initially");
    }

    @Test
    public void testGameRunsThroughAllRounds() {
        // Simulate advancing through all rounds
        for (int i = 0; i < 7; i++) {
            game.nextRound();
        }

        assertTrue(game.getGameOver(), "Game should be over after 7 rounds");
        assertEquals(7, game.getCurrentRoundNumber(), "Game should complete exactly 7 rounds");
    }

    @Test
    public void testWinningConditions() {
        // Simulate conditions required for winning the game

        // Manually set all Landing Gear fields to filled
        for (Field gear : game.getAirplane().getLandingGears().getLandingGearFields()) {
            gear.setFilled(true);
        }

        // Manually set all Flap fields to filled
        for (Field flap : game.getAirplane().getFlaps().getFlapsFields()) {
            flap.setFilled(true);
        }

        // Set engine position and tilt to winning conditions
        game.getAirplane().getEngine().setCurrentPosition(6);
        game.getAirplane().getAxis().setTilt(3);

        // Set the speed below the brake's red marker
        game.getAirplane().getEngine().setSpeed(2);
        game.getAirplane().getBrakes().setRedMarker(3);

        boolean won = game.checkWinningConditions();

        assertTrue(won, "Game should meet winning conditions");
    }

    @Test
    public void testLosingConditions() {
        // Simulate conditions where the game loses

        // Leave Landing Gear fields unfilled
        for (Field gear : game.getAirplane().getLandingGears().getLandingGearFields()) {
            gear.setFilled(false);
        }

        // Simulate an invalid engine position
        game.getAirplane().getEngine().setCurrentPosition(3);

        boolean won = game.checkWinningConditions();
        assertFalse(won, "Game should fail the winning conditions");
    }

    @Test
    public void testGameEndAfterFinalRound() {
        // Play through all rounds and ensure the game ends

        for (int i = 1; i < 7; i++) {
            game.nextRound();
        }

        game.getGameOver();

        assertTrue(game.getGameOver(), "Game should end after 7 rounds");
    }
}
