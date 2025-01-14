package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.skyteam.logic.GameLogic;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class SkyTeamGame extends Game {
    private SpriteBatch batch;
    private GameLogic gameLogic;
    private OrthographicCamera camera;
    private Viewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();

        setFullscreenMode();
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

    // Use this if you're using a 2k or 4k screen
    private void setWindowedMode() {
        int windowWidth = 1920;
        int windowHeight = 1080;
        Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
        System.out.println("Window resolution set to: " + windowWidth + "x" + windowHeight);
        viewport.update(windowWidth, windowHeight, true);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.apply();
    }

    // Use this if you're using a full HD screen
    private void setFullscreenMode() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        System.out.println("Fullscreen mode set to: " + Gdx.graphics.getDisplayMode().width + "x"
                + Gdx.graphics.getDisplayMode().height);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.apply();
    }
}
