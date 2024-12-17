package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.skyteam.logic.GameLogic;

public class MainMenuScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private final SkyTeamGame game;
    private GameLogic gameLogic;

    public MainMenuScreen(SkyTeamGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Title
        Texture titleTexture = new Texture(Gdx.files.internal("images/title.png"));
        Image titleImage = new Image(titleTexture); // Create an image actor

        // Position the title image above the buttons
        titleImage.setPosition(
            (Gdx.graphics.getWidth() - titleImage.getWidth()) / 2f,
            Gdx.graphics.getHeight() - titleImage.getHeight() - 150 // Adjust Y position to leave space for buttons
        );

        // Start Button
        Texture normalStartTexture = new Texture(Gdx.files.internal("buttons/button_start_normal.png"));
        Texture pressedStartTexture = new Texture(Gdx.files.internal("buttons/button_start_pressed.png"));

        TextureRegionDrawable normalStartDrawable = new TextureRegionDrawable(new TextureRegion(normalStartTexture));
        TextureRegionDrawable pressedStartDrawable = new TextureRegionDrawable(new TextureRegion(pressedStartTexture));

        ImageButton startButton = new ImageButton(normalStartDrawable, pressedStartDrawable);
        startButton.setSize(400, 80);
        startButton.setPosition(
                (Gdx.graphics.getWidth() - startButton.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - startButton.getHeight()) / 2f + 40); // Centered

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.startGame();
            }
        });

        // Exit Button
        Texture normalQuitTexture = new Texture(Gdx.files.internal("buttons/button_quit_normal.png"));
        Texture pressedQuitTexture = new Texture(Gdx.files.internal("buttons/button_quit_pressed.png"));

        TextureRegionDrawable normalQuitDrawable = new TextureRegionDrawable(new TextureRegion(normalQuitTexture));
        TextureRegionDrawable pressedQuitDrawable = new TextureRegionDrawable(new TextureRegion(pressedQuitTexture));

        ImageButton quitButton = new ImageButton(normalQuitDrawable, pressedQuitDrawable);
        quitButton.setSize(400, 80);
        quitButton.setPosition(
                (Gdx.graphics.getWidth() - quitButton.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - quitButton.getHeight()) / 2f - 100);

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(titleImage);
        stage.addActor(startButton);
        stage.addActor(quitButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.961f, 0.776f, 0.435f, 1f);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update stage on resize
    }

    @Override
    public void hide() {
        stage.dispose(); // Dispose when hidden
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}