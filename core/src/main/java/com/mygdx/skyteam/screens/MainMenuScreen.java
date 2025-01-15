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
import com.mygdx.skyteam.screens.InstructionScreen; 


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

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Title
        Texture titleTexture = new Texture(Gdx.files.internal("images/title.png"));
        Image titleImage = new Image(titleTexture);

        titleImage.setPosition(
                (Gdx.graphics.getWidth() - titleImage.getWidth()) / 2f,
                Gdx.graphics.getHeight() - titleImage.getHeight() - 450);

        // Title Border
        Texture titleBorderTexture = new Texture(Gdx.files.internal("images/mainmenu_border.png"));
        Image titleBorderImage = new Image(titleBorderTexture);
        titleBorderImage.setPosition(
                (Gdx.graphics.getWidth() - titleBorderImage.getWidth()) / 2f,
                Gdx.graphics.getHeight() - titleBorderImage.getHeight() - 100);

        // Plane Icon
        Texture planeIconTexture = new Texture(Gdx.files.internal("images/plane_icon.png"));
        Image planeIcon = new Image(planeIconTexture);
        planeIcon.setSize(450, 450);
        planeIcon.setPosition(
                titleImage.getX() + 700,
                titleImage.getY() + 100);

        // For positioning
        float buttonY = 200f;

        float centerX = (Gdx.graphics.getWidth() - 400) / 2f;
        float startX = centerX - 500f;
        float howToPlayX = centerX;
        float quitX = centerX + 500f;

        // Play Button
        Texture normalStartTexture = new Texture(Gdx.files.internal("buttons/play_button.png"));
        Texture pressedStartTexture = new Texture(Gdx.files.internal("buttons/play_button_pressed.png"));

        TextureRegionDrawable normalStartDrawable = new TextureRegionDrawable(new TextureRegion(normalStartTexture));
        TextureRegionDrawable pressedStartDrawable = new TextureRegionDrawable(new TextureRegion(pressedStartTexture));

        ImageButton startButton = new ImageButton(normalStartDrawable, pressedStartDrawable);
        startButton.setPosition(startX, buttonY);

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
        howToPlayButton.setPosition(howToPlayX, buttonY);

        howToPlayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InstructionScreen(game));
            }
        });

        // Exit Button
        Texture normalQuitTexture = new Texture(Gdx.files.internal("buttons/quit_button.png"));
        Texture pressedQuitTexture = new Texture(Gdx.files.internal("buttons/quit_button_pressed.png"));

        TextureRegionDrawable normalQuitDrawable = new TextureRegionDrawable(new TextureRegion(normalQuitTexture));
        TextureRegionDrawable pressedQuitDrawable = new TextureRegionDrawable(new TextureRegion(pressedQuitTexture));

        ImageButton quitButton = new ImageButton(normalQuitDrawable, pressedQuitDrawable);
        quitButton.setPosition(quitX, buttonY);

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        stage.addActor(planeIcon);
        stage.addActor(titleBorderImage);
        stage.addActor(titleImage);
        stage.addActor(startButton);
        stage.addActor(howToPlayButton);
        stage.addActor(quitButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.9843f, 0.6667f, 0.0980f, 1f); 
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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