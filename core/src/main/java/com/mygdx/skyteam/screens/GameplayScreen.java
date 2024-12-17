package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.skyteam.logic.Dice;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import com.mygdx.skyteam.logic.GameLogic;

import com.mygdx.skyteam.logic.Field;

public class GameplayScreen implements Screen {

    private Stage stage;
    private SkyTeamGame game;
    private GameLogic gameLogic;
    private Texture boardTexture, planeTrack, altitudeTrack, axisIcon;
    private SpriteBatch batch;
    private Sprite axisSprite;

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

    private Texture switchTrack;
    private Texture redMarkerTrack;
    private Texture blueMarkerTrack;
    private Texture orangeMarkerTrack;
    private Texture coffeeTrack;
    private ArrayList<Boolean> switchesStates;
    private ArrayList<Vector2> switchesPositions;
    private ArrayList<Vector2> coffeesPositions;
    private Vector2 blueMarkerPosition;
    private int currentBlueMarkerSteps = 0;
    private int currentRedMarkerSteps = 0;
    private int currentOrangeMarkerSteps = 0;
    private int currentCoffeeQuantity=0;
    private Vector2 redMarkerPosition;
    private Vector2 orangeMarkerPosition;
    public GameplayScreen(SkyTeamGame game, GameLogic gameLogic) {
        this.game = game;
        this.gameLogic = gameLogic;
    }

    @Override
    public void show() {
        boardTexture = new Texture(Gdx.files.internal("board/Control Panel.png"));
        planeTrack = new Texture(Gdx.files.internal("board/Track.png"));
        altitudeTrack = new Texture(Gdx.files.internal("board/Altitude.png"));
        axisIcon = new Texture(Gdx.files.internal("board/axis_icon.png"));
        switchTrack = new Texture(Gdx.files.internal("board/icons/Switch.png"));
        blueMarkerTrack = new Texture(Gdx.files.internal("board/markers/MarkerBlue.png"));
        redMarkerTrack = new Texture(Gdx.files.internal("board/markers/MarkerRed.png"));
        orangeMarkerTrack = new Texture(Gdx.files.internal("board/markers/MarkerOrange.png"));
        coffeeTrack = new Texture(Gdx.files.internal("board/icons/Coffee.png"));
        axisSprite = new Sprite(axisIcon);
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

        switchesPositions = new ArrayList<>();
        switchesStates = new ArrayList<>();
        for(int i=0; i<10; i++){
            switchesStates.add(false);
        }



        switchesPositions = new ArrayList<>();

        // Switches for Landing gears
        switchesPositions.add(new Vector2(732,340));
        switchesPositions.add(new Vector2(732, 236));
        switchesPositions.add(new Vector2(732, 127));

        // Switches for Flaps
        switchesPositions.add(new Vector2(1138, 340));
        switchesPositions.add(new Vector2(1138, 236));
        switchesPositions.add(new Vector2(1138, 127));
        switchesPositions.add(new Vector2(1138, 20));

        // Switches for Brakes
        switchesPositions.add(new Vector2(867, 100));
        switchesPositions.add(new Vector2(940, 100));
        switchesPositions.add(new Vector2(1012, 100));

        // Blue Markers
        blueMarkerPosition = new Vector2(865, 370);

        // Red Markers
        redMarkerPosition = new Vector2(864, 242);

        // Orange Markers

        orangeMarkerPosition = new Vector2(1014, 353);

        // Coffee positions list
        coffeesPositions = new ArrayList<>();
        coffeesPositions.add(new Vector2(763,67));
        coffeesPositions.add(new Vector2(763, 26));
        coffeesPositions.add(new Vector2(811, 25));

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
        drawSwitchesAndMarkers();
        axisSprite.draw(batch);
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

        float rotationAngle = gameLogic.getAirplane().getAxis().getRotationAngle();

        float axisIconWidth = axisIcon.getWidth() * scaleFactor;
        float axisIconHeight = axisIcon.getHeight() * scaleFactor;
        float axisIconX = boardX + (boardWidth - axisIconWidth) / 2f;
        float axisIconY = boardY + (boardHeight - axisIconHeight) / 2f + 150;

        axisSprite.setSize(axisIconWidth, axisIconHeight);
        axisSprite.setPosition(axisIconX, axisIconY);
        axisSprite.setOrigin(axisSprite.getWidth() / 2, axisSprite.getHeight() / 2);
        axisSprite.setRotation(rotationAngle);

    }

    private void drawSwitchesAndMarkers() {

        //Draw Switches
        for(int i =0;i<switchesPositions.size();i++){
            Vector2 position = switchesPositions.get(i);
            boolean isSwitchOn = switchesStates.get(i);

            float switchX = isSwitchOn ? position.x - 9 : position.x + 20;
            float switchY = position.y;
            float switchSize = 28;

            batch.draw(switchTrack, switchX, switchY, switchSize, switchSize);
        }
        // Draw blue marker
        batch.draw(blueMarkerTrack, blueMarkerPosition.x, blueMarkerPosition.y, 13, 13);
        // Draw red marker
        batch.draw(redMarkerTrack, redMarkerPosition.x, redMarkerPosition.y, 13,13);
        // Draw orange marker
        batch.draw(orangeMarkerTrack, orangeMarkerPosition.x, orangeMarkerPosition.y, 13,13);

        // Draw coffee
        for(int i=0; i<currentCoffeeQuantity;i++){
            Vector2 position = coffeesPositions.get(i);

            batch.draw(coffeeTrack, position.x, position.y,28,28);

        }

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
                    fieldChoice = i + 1;
                    if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        switchesStates.set(i+7,true);
                        currentRedMarkerSteps++;
                        if(currentRedMarkerSteps==1){
                            redMarkerPosition.set(900,220);
                        } else if (currentRedMarkerSteps==2){
                            redMarkerPosition.set(975,207);
                        } else if (currentRedMarkerSteps==3){
                            redMarkerPosition.set(1046,243);
                        }
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
                    playerInput = "coffee";
                    fieldChoice = i + 1;
                    if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        currentCoffeeQuantity++;
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
                    playerInput = "landing gears";
                    fieldChoice = i + 1;
                    if (gameLogic.getPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        switchesStates.set(i,true);
                        currentBlueMarkerSteps++;
                        if(currentBlueMarkerSteps==1){
                            blueMarkerPosition.set(900,350);
                        } else if (currentBlueMarkerSteps==2){
                            blueMarkerPosition.set(935,340);
                        } else if (currentBlueMarkerSteps==3){
                            blueMarkerPosition.set(975,345);
                        }
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

                    playerInput = "flaps";
                    fieldChoice = i + 1;
                    Vector3 fieldPosition = gameLogic.getAirplane().getFlaps().getFlapsFields().get(i)
                            .getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    if (gameLogic.getCoPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        switchesStates.set(i+3,true);
                        currentOrangeMarkerSteps++;
                        if(currentOrangeMarkerSteps==1){
                            orangeMarkerPosition.set(1045,375);
                        } else if(currentOrangeMarkerSteps==2){
                            orangeMarkerPosition.set(1070,403);
                        } else if(currentOrangeMarkerSteps==3){
                            orangeMarkerPosition.set(1087,440);
                        } else if(currentOrangeMarkerSteps==4){
                            orangeMarkerPosition.set(1095,477);
                        }
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

                    playerInput = "coffee";
                    fieldChoice = 0;
                    Vector3 fieldPosition = gameLogic.getAirplane().getConcentration().getCoffeeFields().get(i)
                            .getStageCoordinates(stage);
                    float fieldStageX = fieldPosition.x;
                    float fieldStageY = fieldPosition.y;
                    if (gameLogic.getCoPilot().canPlaceDice(diceValue, playerInput, fieldChoice)) {
                        diceImage.setPosition(fieldStageX, fieldStageY - 50);
                        gameLogic.getRound().collectPlayerInput(diceIndex, playerInput, fieldChoice);
                        currentCoffeeQuantity++;
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
