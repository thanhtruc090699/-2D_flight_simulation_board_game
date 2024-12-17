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

    private ArrayList<Texture> pilotDiceTextures;
    private ArrayList<Texture> coPilotDiceTextures;
    private ArrayList<Dice> pilotDice;
    private ArrayList<Dice> coPilotDice;
    private int playerIndex;

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

        for (int i = 1; i <= 6; i++) {
            pilotDiceTextures.add(new Texture("dice/" + i + "B.png"));
            coPilotDiceTextures.add(new Texture("dice/" + i + "R.png"));
        }

        gameLogic.startGame();
        playerIndex = gameLogic.getRound().getCurrentPlayerIndex();

        pilotDice = new ArrayList<>(gameLogic.getPilot().getDices());
        coPilotDice = new ArrayList<>(gameLogic.getCoPilot().getDices());

        drawDice(coPilotDice, coPilotDiceTextures, false);
        drawDice(pilotDice, pilotDiceTextures, true);

    }

    public void drawDice(ArrayList<Dice> diceList, ArrayList<Texture> diceTextures, boolean isPilot) {

        for (int i = 0; i < diceList.size(); i++) {
            final Dice dice = diceList.get(i);
            final Image diceImage = new Image(diceTextures.get(dice.getDiceValue() - 1));

            diceImage.setPosition(isPilot ? pilotStartX + i * diceSpacing : coPilotStartX + i * diceSpacing,
                    isPilot ? pilotStartY : coPilotStartY);
            diceImage.setSize(50, 50);

            stage.addActor(diceImage);

            diceImage.clearListeners();

            if (isPilot) {
                System.out.println("Adding Pilot Listener");
                handlePilotInteraction(dice, diceImage);
            } else if (!isPilot) {
                System.out.println("Adding Co-Pilot Listener");
                handleCoPilotInteraction(dice, diceImage);
            }
        }
    }

    private void handlePilotInteraction(final Dice dice, final Image diceImage) {
        diceImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                System.out.println("Pilot Dice touchDown");
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
                    diceImage.setPosition(newX, newY);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                isDraggingPilotDice = false;
                int diceIndex = pilotDice.indexOf(draggedPilotDice);
                handleInput((int) event.getStageX(), (int) event.getStageY(), true, diceImage, diceIndex);
            }

        });
    }

    private void handleCoPilotInteraction(final Dice dice, final Image diceImage) {
        diceImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Co-Pilot Dice touchDown");
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
                    diceImage.setPosition(newX, newY);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                isDraggingCoPilotDice = false;
                int diceIndex = coPilotDice.indexOf(draggedCoPilotDice);
                handleInput((int) event.getStageX(), (int) event.getStageY(), false, diceImage, diceIndex);
            }
        });
    }

    @Override
    public void render(float delta) {

        /*
         * // Testing purposes
         * float screenX = Gdx.input.getX();
         * float screenY = Gdx.input.getY();
         * System.out.println("Screen Position - X: " + screenX + ", Y: " + screenY);
         */

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();

        drawBoardAndTracks();

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    private void drawBoardAndTracks() {
        float scaleFactor = 0.8f;

        float planeTrackWidth = planeTrack.getWidth() * scaleFactor - 6;
        float planeTrackHeight = planeTrack.getHeight() * scaleFactor;
        float planeTrackX = (Gdx.graphics.getWidth() - planeTrackWidth) / 2f - 95 + 3;
        float planeTrackY = boardTexture.getHeight() * scaleFactor - 100;
        batch.draw(planeTrack, planeTrackX, planeTrackY, planeTrackWidth, planeTrackHeight);

        float altitudeTrackWidth = altitudeTrack.getWidth() * scaleFactor - 6;
        float altitudeTrackHeight = altitudeTrack.getHeight() * scaleFactor;
        float altitudeTrackX = (Gdx.graphics.getWidth() - altitudeTrackWidth) / 2f + 95 - 2;
        batch.draw(altitudeTrack, altitudeTrackX, planeTrackY, altitudeTrackWidth, altitudeTrackHeight);

        float boardWidth = boardTexture.getWidth() * scaleFactor;
        float boardHeight = boardTexture.getHeight() * scaleFactor;
        float boardX = (Gdx.graphics.getWidth() - boardWidth) / 2f;
        float boardY = 0;
        batch.draw(boardTexture, boardX, boardY, boardWidth, boardHeight);
    }

    public void handleInput(int mouseX, int mouseY, boolean isPilot, Image diceImage, int diceIndex) {

        String playerInput = "";
        int fieldChoice = -1;
        int diceValue = isPilot ? pilotDice.get(diceIndex).getDiceValue()
                : coPilotDice.get(diceIndex).getDiceValue();

        if (isPilot) {
            // Axis
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getAxis().getPilotAxisField(),
                    diceImage)) {
                playerInput = "axis";
                fieldChoice = 0;
                Vector3 fieldPosition = gameLogic.getAirplane().getAxis().getPilotAxisField()
                        .getStageCoordinates(stage);
                float fieldStageX = fieldPosition.x;
                float fieldStageY = fieldPosition.y;
                if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                    diceImage.setPosition(fieldStageX, fieldStageY - 50);
                    gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                    gameLogic.getRound().playRound();
                } else {
                    diceImage.setPosition(pilotStartX + diceIndex * diceSpacing, pilotStartY);
                }
            }

            // Engine
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getEngine().getPilotField(), diceImage)) {
                playerInput = "engine";
                fieldChoice = 0;
                Vector3 fieldPosition = gameLogic.getAirplane().getEngine().getPilotField().getStageCoordinates(stage);
                float fieldStageX = fieldPosition.x;
                float fieldStageY = fieldPosition.y;
                if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                    diceImage.setPosition(fieldStageX, fieldStageY - 50);
                    gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                    gameLogic.getRound().playRound();
                } else {
                    diceImage.setPosition(pilotStartX + diceIndex * diceSpacing, pilotStartY);
                }
            }

            // Brake
            for (int i = 0; i < 3; i++) {
                if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getBrakes().getBrakeFields().get(i),
                        diceImage)) {
                    Vector3 fieldPosition = gameLogic.getAirplane().getBrakes().getBrakeFields().get(i)
                            .getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    playerInput = "brake";
                    fieldChoice = 0;
                    if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        gameLogic.getRound().playRound();
                    } else {
                        diceImage.setPosition(pilotStartX + diceIndex * diceSpacing, pilotStartY);
                    }
                    break;
                }
            }

            // Concentration (Coffee)
            for (int i = 0; i < 3; i++) {
                if (isMouseOverField(mouseX, mouseY,
                        gameLogic.getAirplane().getConcentration().getCoffeeFields().get(i), diceImage)) {
                    Vector3 fieldPosition = gameLogic.getAirplane().getConcentration().getCoffeeFields().get(i)
                            .getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    playerInput = "concentration";
                    fieldChoice = 0;
                    if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        gameLogic.getRound().playRound();
                    } else {
                        diceImage.setPosition(pilotStartX + diceIndex * diceSpacing, pilotStartY);
                    }
                    break;
                }
            }

            // Landing Gear
            for (int i = 0; i < 3; i++) {
                if (isMouseOverField(mouseX, mouseY,
                        gameLogic.getAirplane().getLandingGears().getLandingGearFields().get(i), diceImage)) {
                    Vector3 fieldPosition = gameLogic.getAirplane().getLandingGears().getLandingGearFields().get(i)
                            .getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    playerInput = "landingGear";
                    fieldChoice = i + 1;
                    if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        gameLogic.getRound().playRound();
                    } else {
                        diceImage.setPosition(pilotStartX + diceIndex * diceSpacing, pilotStartY);
                    }
                    break;
                }
            }

            // Radio
            if (isMouseOverField(mouseX, mouseY, gameLogic.getPilot().getRadio().getRadioFields().get(0), diceImage)) {
                Vector3 fieldPosition = gameLogic.getPilot().getRadio().getRadioFields().get(0)
                        .getStageCoordinates(stage);
                float fieldStageX = fieldPosition.x;
                float fieldStageY = fieldPosition.y;
                playerInput = "radio";
                fieldChoice = 0;
                if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                    diceImage.setPosition(fieldStageX, fieldStageY - 50);
                    gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                    gameLogic.getRound().playRound();
                } else {
                    diceImage.setPosition(pilotStartX + diceIndex * diceSpacing, pilotStartY);
                }
            }
        }
        if (!isPilot) {
            // Axis
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getAxis().getCoPilotAxisField(), diceImage)) {
                playerInput = "axis";
                fieldChoice = 0;
                Vector3 fieldPosition = gameLogic.getAirplane().getAxis().getCoPilotAxisField()
                        .getStageCoordinates(stage);
                float fieldStageX = fieldPosition.x;
                float fieldStageY = fieldPosition.y;
                if (gameLogic.getCoPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                    diceImage.setPosition(fieldStageX, fieldStageY - 50);
                    gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                    gameLogic.getRound().playRound();
                } else {
                    diceImage.setPosition(coPilotStartX + diceIndex * diceSpacing, coPilotStartY);
                }

            }

            // Engine
            if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getEngine().getCoPilotField(), diceImage)) {

                playerInput = "engine";
                fieldChoice = 0;
                Vector3 fieldPosition = gameLogic.getAirplane().getEngine().getCoPilotField()
                        .getStageCoordinates(stage);
                float fieldStageX = fieldPosition.x;
                float fieldStageY = fieldPosition.y;
                if (gameLogic.getCoPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                    diceImage.setPosition(fieldStageX, fieldStageY - 50);
                    gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                    gameLogic.getRound().playRound();
                } else {
                    diceImage.setPosition(coPilotStartX + diceIndex * diceSpacing, coPilotStartY);
                }
            }

            // Flaps
            for (int i = 0; i < 4; i++) {
                if (isMouseOverField(mouseX, mouseY, gameLogic.getAirplane().getFlaps().getFlapsFields().get(i),
                        diceImage)) {

                    playerInput = "flap";
                    fieldChoice = i + 1;
                    Vector3 fieldPosition = gameLogic.getAirplane().getFlaps().getFlapsFields().get(i)
                            .getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    if (gameLogic.getCoPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        gameLogic.getRound().playRound();
                    } else {
                        diceImage.setPosition(coPilotStartX + diceIndex * diceSpacing, coPilotStartY);
                    }
                    break;
                }
            }

            // Radio
            for (int i = 0; i < 2; i++) {
                if (isMouseOverField(mouseX, mouseY, gameLogic.getCoPilot().getRadio().getRadioFields().get(i),
                        diceImage)) {

                    playerInput = "radio";
                    fieldChoice = i;
                    Vector3 fieldPosition = gameLogic.getCoPilot().getRadio().getRadioFields().get(i)
                            .getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    if (gameLogic.getCoPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        gameLogic.getRound().playRound();
                    } else {
                        diceImage.setPosition(coPilotStartX + diceIndex * diceSpacing, coPilotStartY);
                    }
                    break;
                }
            }

            // Concentration (Coffee)
            for (int i = 0; i < 3; i++) {
                if (isMouseOverField(mouseX, mouseY,
                        gameLogic.getAirplane().getConcentration().getCoffeeFields().get(i), diceImage)) {

                    playerInput = "concentration";
                    fieldChoice = 0;
                    Vector3 fieldPosition = gameLogic.getAirplane().getConcentration().getCoffeeFields().get(i).getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    if (gameLogic.getCoPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        gameLogic.getRound().playRound();
                    } else {
                        diceImage.setPosition(coPilotStartX + diceIndex * diceSpacing, coPilotStartY);
                    }
                    break;
                }
            }
        }

        if (playerInput.isEmpty()) {
            if (isPilot && draggedPilotDice != null) {
                diceImage.setPosition(pilotStartX + pilotDice.indexOf(draggedPilotDice) * diceSpacing, pilotStartY);
            } else if (!isPilot && draggedCoPilotDice != null) {
                diceImage.setPosition(coPilotStartX + coPilotDice.indexOf(draggedCoPilotDice) * diceSpacing,
                        coPilotStartY);
            }
        }

    }

    public boolean isMouseOverField(int mouseX, int mouseY, Field field, Image diceImage) {
        float stageX = stage.getCamera().unproject(new com.badlogic.gdx.math.Vector3(mouseX, mouseY, 0)).x;
        float stageY = stage.getCamera().unproject(new com.badlogic.gdx.math.Vector3(mouseX, mouseY, 0)).y;

        Vector3 fieldPosition = field.getStageCoordinates(stage);
        float fieldStageX = fieldPosition.x;
        float fieldStageY = fieldPosition.y;

        if (field.isMouseOver(stageX, stageY)) {
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
