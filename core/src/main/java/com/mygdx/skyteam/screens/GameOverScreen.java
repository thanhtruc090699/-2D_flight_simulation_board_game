package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
/**
 * Represents the Game Over screen in the game.
 * Displays options to retry the game or quit to the main menu.
 */
public class GameOverScreen implements Screen {
    private final SkyTeamGame game;
    private final SpriteBatch batch;

    private final Texture background;
    private final Texture title;
    private final Texture retryButton;
    private final Texture quitButton;

    private final float retryButtonX, retryButtonY;
    private final float quitButtonX, quitButtonY;

    public GameOverScreen(SkyTeamGame game) {
        this.game = game;
        this.batch = new SpriteBatch();

        this.background = new Texture(Gdx.files.internal("images/gameover.jpg"));
        this.title = new Texture(Gdx.files.internal("images/gameover_title.png"));
        this.retryButton = new Texture(Gdx.files.internal("images/retry_button.png"));
        this.quitButton = new Texture(Gdx.files.internal("images/quit_button.png"));

        this.retryButtonX = 20;
        this.retryButtonY = 120;
        this.quitButtonX = 20;
        this.quitButtonY = 30;
    }

    public void show() {

    }

    public void render(float delta) {
        handleInput();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(title,
                (Gdx.graphics.getWidth() - title.getWidth()) / 2f,
                Gdx.graphics.getHeight() - title.getHeight() - 100);
        batch.draw(retryButton, retryButtonX, retryButtonY);
        batch.draw(quitButton, quitButtonX, quitButtonY);
        batch.end();

    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {

            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (touchX >= retryButtonX && touchX <= retryButtonX + retryButton.getWidth() &&
                    touchY >= retryButtonY && touchY <= retryButtonY + retryButton.getHeight()) {
                game.setScreen(new MainMenuScreen(game));
            }

            if (touchX >= quitButtonX && touchX <= quitButtonX + quitButton.getWidth() &&
                    touchY >= quitButtonY && touchY <= quitButtonY + quitButton.getHeight()) {
                Gdx.app.exit();
            }
        }
    }

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {
        batch.dispose();
        background.dispose();
        title.dispose();
        retryButton.dispose();
        quitButton.dispose();
    }
}
