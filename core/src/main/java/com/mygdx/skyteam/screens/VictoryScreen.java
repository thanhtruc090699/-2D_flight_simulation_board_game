package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
/**
 * Represents the victory screen displayed after the player wins the game.
 * Provides options to play again or quit the application.
 */
public class VictoryScreen extends ScreenAdapter {

    private final SkyTeamGame game;
    private SpriteBatch batch;
    private Texture background;
    private Texture title;
    private Texture playAgainButton;
    private Texture quitButton;

    private final float playAgainButtonX, playAgainButtonY;
    private final float quitButtonX, quitButtonY;
    private final float titleX, titleY;

    public VictoryScreen(SkyTeamGame game) {
        this.game = game;

        this.background = new Texture(Gdx.files.internal("images/victory.jpg"));
        this.title = new Texture(Gdx.files.internal("images/victory_title.png"));
        this.playAgainButton = new Texture(Gdx.files.internal("images/play_v.png"));
        this.quitButton = new Texture(Gdx.files.internal("images/quit_v.png"));

        this.playAgainButtonX = Gdx.graphics.getWidth() - playAgainButton.getWidth() - 20;
        this.playAgainButtonY = 90;
        this.quitButtonX = Gdx.graphics.getWidth() - quitButton.getWidth() - 20;
        this.quitButtonY = 30;

        this.titleX = Gdx.graphics.getWidth() - title.getWidth() - 20;
        this.titleY = Gdx.graphics.getHeight() - title.getHeight() - 100;
    }


    @Override
    public void show() {
        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(title, titleX, titleY);

        batch.draw(playAgainButton, playAgainButtonX, playAgainButtonY);
        batch.draw(quitButton, quitButtonX, quitButtonY);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the "Play Again" button was pressed
            if (touchX >= playAgainButtonX && touchX <= playAgainButtonX + playAgainButton.getWidth() &&
                    touchY >= playAgainButtonY && touchY <= playAgainButtonY + playAgainButton.getHeight()) {
                // Replace with the screen you want to go to (e.g., the game screen)
                game.setScreen(new MainMenuScreen(game)); // Change this to the appropriate screen
            }

            // Check if the "Quit" button was pressed
            if (touchX >= quitButtonX && touchX <= quitButtonX + quitButton.getWidth() &&
                    touchY >= quitButtonY && touchY <= quitButtonY + quitButton.getHeight()) {
                Gdx.app.exit();  // Exit the application
            }
        }
    }

    @Override
    public void hide() {
        batch.dispose();
        background.dispose();
        title.dispose();
        playAgainButton.dispose();
        quitButton.dispose();
    }
}
