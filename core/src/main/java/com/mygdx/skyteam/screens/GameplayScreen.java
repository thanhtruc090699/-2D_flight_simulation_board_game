package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.skyteam.logic.Dice;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import com.mygdx.skyteam.logic.GameLogic;

import com.mygdx.skyteam.logic.Field;

public class GameplayScreen implements Screen {

    private Stage stage;
    private SkyTeamGame game;
    private GameLogic gameLogic;
    private Texture boardTexture;
    private Texture planeTrack;
    private Texture altitudeTrack;
    private SpriteBatch batch;

    float pilotStartX = 300;
    float pilotStartY = 100;
    float diceSpacing = 60;
    float coPilotStartX = 1300;
    float coPilotStartY = 100;

    private boolean isDraggingPilotDice = false;
    private boolean isDraggingCoPilotDice = false;
    private Dice draggedPilotDice = null;
    private Dice draggedCoPilotDice = null;
    private float dragStartX = 0;
    private float dragStartY = 0;
    private Image pilotDiceImage;
    private Image coPilotDiceImage;

    private ArrayList<Texture> pilotDiceTextures;
    private ArrayList<Texture> coPilotDiceTextures;
    private ArrayList<Dice> pilotDice;
    private ArrayList<Dice> coPilotDice;

    public GameplayScreen(SkyTeamGame game, GameLogic gameLogic) {
        this.game = game;
        this.gameLogic = gameLogic;
    }

    @Override
    public void show() {
        boardTexture = new Texture(Gdx.files.internal("board/Control Panel.png"));
        planeTrack = new Texture(Gdx.files.internal("board/Track.png"));
        altitudeTrack = new Texture(Gdx.files.internal("board/Altitude.png"));

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        pilotDiceTextures = new ArrayList<>();
        coPilotDiceTextures = new ArrayList<>();

        // Load dice textures into an array
        for (int i = 1; i <= 6; i++) {
            pilotDiceTextures.add(new Texture("dice/" + i + "B.png"));
            coPilotDiceTextures.add(new Texture("dice/" + i + "R.png"));
        }

        gameLogic.startGame();

        pilotDice = new ArrayList<>(gameLogic.getPilot().getDices());
        coPilotDice = new ArrayList<>(gameLogic.getCoPilot().getDices());

        // Drawing dices
        for (int i = 0; i < pilotDice.size(); i++) {
            final Dice dice = pilotDice.get(i);
            final Image pilotDiceImage = new Image(pilotDiceTextures.get(dice.getDiceValue() - 1));
            pilotDiceImage.setPosition(pilotStartX + i * diceSpacing, pilotStartY);
            pilotDiceImage.setSize(50, 50);

            pilotDiceImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    isDraggingPilotDice = true;
                    draggedPilotDice = dice;
                    dragStartX = x;
                    dragStartY = y;
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (isDraggingPilotDice) {
                        float newX = event.getStageX() - dragStartX;
                        float newY = event.getStageY() - dragStartY;
                        pilotDiceImage.setPosition(newX, newY);
                    }
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                    isDraggingPilotDice = false;
                    handleInput((int) event.getStageX(), (int) event.getStageY(), true, pilotDiceImage);
                }
            });
            stage.addActor(pilotDiceImage);

        }

        for (int i = 0; i < coPilotDice.size(); i++) {
            final Dice dice = coPilotDice.get(i);
            final Image coPilotDiceImage = new Image(coPilotDiceTextures.get(dice.getDiceValue() - 1));
            coPilotDiceImage.setPosition(coPilotStartX + i * diceSpacing, coPilotStartY);
            coPilotDiceImage.setSize(50, 50);

            coPilotDiceImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    isDraggingCoPilotDice = true;
                    draggedCoPilotDice = dice;
                    dragStartX = x;
                    dragStartY = y;
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (isDraggingCoPilotDice) {
                        float newX = event.getStageX() - dragStartX;
                        float newY = event.getStageY() - dragStartY;
                        coPilotDiceImage.setPosition(newX, newY);
                    }
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    isDraggingCoPilotDice = false;
                    handleInput((int) event.getStageX(), (int) event.getStageY(), false, coPilotDiceImage);

                }
            });
            stage.addActor(coPilotDiceImage);
        }

    }

    @Override
    public void render(float delta) {

        // Testing purposes
        float screenX = Gdx.input.getX();
        float screenY = Gdx.input.getY();
        System.out.println("Screen Position - X: " + screenX + ", Y: " + screenY);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();

        drawBoardAndTracks();

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    private void drawBoardAndTracks() {
        float scaleFactor = 0.8f;

        float planeTrackWidth = planeTrack.getWidth() * scaleFactor;
        float planeTrackHeight = planeTrack.getHeight() * scaleFactor;
        float planeTrackX = (Gdx.graphics.getWidth() - planeTrackWidth) / 2f - 95;
        float planeTrackY = boardTexture.getHeight() * scaleFactor - 100;
        batch.draw(planeTrack, planeTrackX, planeTrackY, planeTrackWidth, planeTrackHeight);

        float altitudeTrackWidth = altitudeTrack.getWidth() * scaleFactor;
        float altitudeTrackHeight = altitudeTrack.getHeight() * scaleFactor;
        float altitudeTrackX = (Gdx.graphics.getWidth() - altitudeTrackWidth) / 2f + 95;
        batch.draw(altitudeTrack, altitudeTrackX, planeTrackY, altitudeTrackWidth, altitudeTrackHeight);

        float boardWidth = boardTexture.getWidth() * scaleFactor;
        float boardHeight = boardTexture.getHeight() * scaleFactor;
        float boardX = (Gdx.graphics.getWidth() - boardWidth) / 2f;
        float boardY = 0;
        batch.draw(boardTexture, boardX, boardY, boardWidth, boardHeight);
    }

    public void handleInput(int mouseX, int mouseY, boolean isPilot, Image diceImage) {

        String playerInput = "";
        int fieldChoice = -1;
        int diceIndex = -1;

        if (isPilot) {
            if (draggedPilotDice != null) {
                diceIndex = pilotDice.indexOf(draggedPilotDice);
            }
        } else {
            if (draggedCoPilotDice != null) {
                diceIndex = coPilotDice.indexOf(draggedCoPilotDice);
            }
        }

        if (isPilot) {
            // Axis
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getAxis().getPilotAxisField(),
                    diceImage)) {
                playerInput = "axis";
                fieldChoice = 0;
            }

            // Engine
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getEngine().getPilotField(), diceImage)) {
                playerInput = "engine";
                fieldChoice = 0;
            }

            // Brake
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getBrakes().getBrakeFields().get(0),
                    diceImage)) {
                playerInput = "brake";
                fieldChoice = 0;
            } else if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getBrakes().getBrakeFields().get(1),
                    diceImage)) {
                playerInput = "brake";
                fieldChoice = 0;
            } else if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getBrakes().getBrakeFields().get(2),
                    diceImage)) {
                playerInput = "brake";
                fieldChoice = 0;
            }

            // Concentration (Coffee)
            if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getConcentration().getCoffeeFields().get(0),
                    diceImage)) {
                playerInput = "concentration";
                fieldChoice = 0;
            } else if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getConcentration().getCoffeeFields().get(1), diceImage)) {
                playerInput = "concentration";
                fieldChoice = 0;
            } else if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getConcentration().getCoffeeFields().get(2), diceImage)) {
                playerInput = "concentration";
                fieldChoice = 0;
            }

            // Landing Gear
            if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getLandingGears().getLandingGearFields().get(0), diceImage)) {
                playerInput = "landingGear";
                fieldChoice = 1;
            } else if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getLandingGears().getLandingGearFields().get(1), diceImage)) {
                playerInput = "landingGear";
                fieldChoice = 2;
            } else if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getLandingGears().getLandingGearFields().get(2), diceImage)) {
                playerInput = "landingGear";
                fieldChoice = 3;
            }

            // Radio
            if (isMouseOverField(mouseX, mouseY, gameLogic.getPilot().getRadio().getRadioFields().get(0),
                    diceImage)) {
                playerInput = "radio";
                fieldChoice = 0;
            }
        }
        if (!isPilot) {
            // Axis
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getAxis().getCoPilotAxisField(),
                    diceImage)) {
                playerInput = "axis";
                fieldChoice = 0;
            }

            // Engine
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getEngine().getCoPilotField(),
                    diceImage)) {
                playerInput = "engine";
                fieldChoice = 0;
            }

            // Flaps
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getFlaps().getFlapsFields().get(0),
                    diceImage)) {
                playerInput = "flap";
                fieldChoice = 1;
            } else if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getFlaps().getFlapsFields().get(1),
                    diceImage)) {
                playerInput = "flap";
                fieldChoice = 2;
            } else if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getFlaps().getFlapsFields().get(2),
                    diceImage)) {
                playerInput = "flap";
                fieldChoice = 3;
            } else if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getFlaps().getFlapsFields().get(3),
                    diceImage)) {
                playerInput = "flap";
                fieldChoice = 4;
            }

            // Radio
            if (isMouseOverField(mouseX, mouseY, gameLogic.getCoPilot().getRadio().getRadioFields().get(0),
                    diceImage)) {
                playerInput = "radio";
                fieldChoice = 0;
            } else if (isMouseOverField(mouseX, mouseY, gameLogic.getCoPilot().getRadio().getRadioFields().get(1),
                    diceImage)) {
                playerInput = "radio";
                fieldChoice = 1;
            }

            // Concentration (Coffee)
            if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getConcentration().getCoffeeFields().get(0),
                    diceImage)) {
                playerInput = "concentration";
                fieldChoice = 0;
            } else if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getConcentration().getCoffeeFields().get(1), diceImage)) {
                playerInput = "concentration";
                fieldChoice = 0;
            } else if (isMouseOverField(mouseX, mouseY,
                    gameLogic.getAirplane().getConcentration().getCoffeeFields().get(2), diceImage)) {
                playerInput = "concentration";
                fieldChoice = 0;
            }
        }

        if (playerInput.isEmpty()) {
            if (isPilot && draggedPilotDice != null) {
                diceImage.setPosition(pilotStartX + pilotDice.indexOf(draggedPilotDice) * diceSpacing, pilotStartY);
            } else if (!isPilot && draggedCoPilotDice != null) {
                diceImage.setPosition(coPilotStartX + coPilotDice.indexOf(draggedCoPilotDice) * diceSpacing,
                        coPilotStartY);
            }

            gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
        }
    }

    public boolean isMouseOverField(int mouseX, int mouseY, Field field, Image diceImage) {
        float stageX = stage.getCamera().unproject(new com.badlogic.gdx.math.Vector3(mouseX, mouseY, 0)).x;
        float stageY = stage.getCamera().unproject(new com.badlogic.gdx.math.Vector3(mouseX, mouseY, 0)).y;

        Vector3 fieldPosition = field.getStageCoordinates(stage);
        float fieldStageX = fieldPosition.x;
        float fieldStageY = fieldPosition.y;

        if (field.isMouseOver(stageX, stageY)) {

            diceImage.setPosition(fieldStageX, fieldStageY - 50);
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update stage on resize
    }

    @Override
    public void hide() {
        dispose(); // Dispose of stage when hidden
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (stage != null)
            stage.dispose();
        if (boardTexture != null)
            boardTexture.dispose();
        if (batch != null)
            batch.dispose();
    }
}
