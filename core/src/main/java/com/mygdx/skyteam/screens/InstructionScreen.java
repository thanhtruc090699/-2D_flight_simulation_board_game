package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * Displays the instruction screen with a manual that users can navigate.
 * Provides "Next" and "Back" buttons to navigate the manual or return to the main menu.
 */
public class InstructionScreen implements Screen {
    private Stage stage;
    private final SkyTeamGame game;
    private Image currentManualImage;
    private int manualPageIndex = 0;

    private String[] manualImages = {
            "images/axis_manual.png",
            "images/engine_manual1.png",
            "images/engine_manual2.png",
            "images/engine_manual3.png",
            "images/radio_manual.png",
            "images/landinggear_manual.png",
            "images/flap_manual.png",
            "images/conc_manual.png",
            "images/brake_manual.png"
    };

    private Texture nextButtonTexture;
    private Texture backButtonTexture;

    public InstructionScreen(SkyTeamGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        currentManualImage = new Image(new Texture(manualImages[manualPageIndex]));

        currentManualImage.setPosition(
                (Gdx.graphics.getWidth() - currentManualImage.getWidth()) / 2,
                (Gdx.graphics.getHeight() - currentManualImage.getHeight()) / 2);

        Texture nextNormalTexture = new Texture(Gdx.files.internal("buttons/next_button.png"));
        TextureRegionDrawable nextNormalDrawable = new TextureRegionDrawable(new TextureRegion(nextNormalTexture));

        ImageButton nextButton = new ImageButton(nextNormalDrawable);
        nextButton.setPosition(Gdx.graphics.getWidth() - nextButton.getWidth() - 10, 10);

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (manualPageIndex < manualImages.length - 1) {
                    manualPageIndex++;
                    currentManualImage.setDrawable(
                            new Image(new Texture(manualImages[manualPageIndex])).getDrawable());
                    currentManualImage.setPosition(
                            (Gdx.graphics.getWidth() - currentManualImage.getWidth()) / 2,
                            (Gdx.graphics.getHeight() - currentManualImage.getHeight()) / 2);
                }

                if (manualPageIndex == manualImages.length - 1) {
                    nextButton.setVisible(false);
                }
            }
        });

        Texture backNormalTexture = new Texture(Gdx.files.internal("buttons/back_button.png"));
        TextureRegionDrawable backNormalDrawable = new TextureRegionDrawable(new TextureRegion(backNormalTexture));

        ImageButton backButton = new ImageButton(backNormalDrawable);
        backButton.setPosition(10, 10);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(currentManualImage);
        stage.addActor(nextButton);
        stage.addActor(backButton);
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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        // Handle resume logic if necessary
    }

    @Override
    public void dispose() {
        // Dispose of resources when the screen is disposed
        stage.dispose();
    }
}
