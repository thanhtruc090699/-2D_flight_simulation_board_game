package com.mygdx.skyteam.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.skyteam.logic.GameLogic;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class SkyTeamGame extends Game {
    private SpriteBatch batch;
    private GameLogic gameLogic;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this)); 
    }

    public void startGame() {
        gameLogic = new GameLogic();  
        this.setScreen(new GameplayScreen(this, gameLogic));  
    }

    @Override
    public void render() {
        super.render(); // Let the current screen handle the rendering
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
