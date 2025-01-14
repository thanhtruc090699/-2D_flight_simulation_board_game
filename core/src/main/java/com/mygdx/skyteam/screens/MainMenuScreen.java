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
    private boolean isFullscreen = false;

    public MainMenuScreen(SkyTeamGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (isFullscreen) {
            setFullscreenMode();
        } else {
            setWindowedMode();
        }

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Title
        Texture titleTexture = new Texture(Gdx.files.internal("images/title.png"));
        Image titleImage = new Image(titleTexture);

        titleImage.setPosition(
                (Gdx.graphics.getWidth() - titleImage.getWidth()) / 2f,
                Gdx.graphics.getHeight() - titleImage.getHeight() - 150);

        // Plane Icon
        Texture planeIconTexture = new Texture(Gdx.files.internal("images/plane_icon.png"));
        Image planeIcon = new Image(planeIconTexture);
        planeIcon.setSize(450, 450);
        planeIcon.setPosition(
                titleImage.getX() + 800,
                titleImage.getY() - 250);

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

        // How to Play Button
        Texture normalHowToPlayTexture = new Texture(Gdx.files.internal("buttons/how_button.png"));
        Texture pressedHowToPlayTexture = new Texture(Gdx.files.internal("buttons/how_button_pressed.png"));

        TextureRegionDrawable normalHowToPlayDrawable = new TextureRegionDrawable(
                new TextureRegion(normalHowToPlayTexture));
        TextureRegionDrawable pressedHowToPlayDrawable = new TextureRegionDrawable(
                new TextureRegion(pressedHowToPlayTexture));

        ImageButton howToPlayButton = new ImageButton(normalHowToPlayDrawable, pressedHowToPlayDrawable);
        howToPlayButton.setSize(400, 80);
        howToPlayButton.setPosition(
                (Gdx.graphics.getWidth() - howToPlayButton.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - howToPlayButton.getHeight()) / 2f - 70); // Positioned between Play and Quit

        howToPlayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Link has to be added to our manual
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
                (Gdx.graphics.getHeight() - quitButton.getHeight()) / 2f - 180);

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        stage.addActor(planeIcon);
        stage.addActor(titleImage);
        stage.addActor(startButton);
        stage.addActor(howToPlayButton);
        stage.addActor(quitButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.961f, 0.776f, 0.435f, 1f);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void setFullscreenMode() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }

    private void setWindowedMode() {
        int windowWidth = 1920;
        int windowHeight = 1080;
        Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
        System.out.println("Window resolution set to: " + windowWidth + "x" + windowHeight);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        stage.dispose();
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