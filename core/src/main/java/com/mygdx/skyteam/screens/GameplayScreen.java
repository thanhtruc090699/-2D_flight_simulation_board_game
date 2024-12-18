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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    private float pilotStartX = 300;
    private float pilotStartY = 100;
    private float diceSpacing = 60;
    private float coPilotStartX = 1300;
    private float coPilotStartY = 100;

    private ArrayList<Texture> pilotDiceTextures;
    private ArrayList<Texture> coPilotDiceTextures;
    private ArrayList<Dice> pilotDice;
    private ArrayList<Dice> coPilotDice;
    private int playerIndex;

    private boolean isDraggingPilotDice = false;
    private boolean isDraggingCoPilotDice = false;
    private Dice draggedPilotDice = null;
    private Dice draggedCoPilotDice = null;
    private float dragStartX = 0;
    private float dragStartY = 0;

    private Texture switchTrack;
    private ArrayList<Boolean> switchesStates;
    private ArrayList<Vector2> switchesPositions;

    private Texture redMarkerTrack;
    private int currentRedMarkerSteps = 0;
    private Vector2 redMarkerPosition;
    private float redMarkerRotation = -40;

    private Texture blueMarkerTrack;
    private Vector2 blueMarkerPosition;
    private int currentBlueMarkerSteps = 0;
    private float blueMarkerRotation = -40;

    private Texture orangeMarkerTrack;
    private int currentOrangeMarkerSteps = 0;
    private Vector2 orangeMarkerPosition;
    private float orangeMarkerRotation = 30;

    private Texture coffeeTrack;
    private ArrayList<Vector2> coffeesPositions;
    private int currentCoffeeQuantity = 0;

    private Texture pilotTurnTexture;
    private Texture coPilotTurnTexture;

    private Texture planeTexture;
    private ArrayList<Vector2> planePositions;

    private float planeTrackVerticalOffset = 0;

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
        pilotTurnTexture = new Texture(Gdx.files.internal("images/pilots_turn.png"));
        coPilotTurnTexture = new Texture(Gdx.files.internal("images/copilots_turn.png"));
        axisSprite = new Sprite(axisIcon);
        planeTexture = new Texture(Gdx.files.internal("board/icons/Plane.png"));
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

        pilotDice = new ArrayList<>(gameLogic.getPilot().getDices());
        coPilotDice = new ArrayList<>(gameLogic.getCoPilot().getDices());

        drawDice(coPilotDice, coPilotDiceTextures, false);
        drawDice(pilotDice, pilotDiceTextures, true);

        switchesPositions = new ArrayList<>();
        switchesStates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            switchesStates.add(false);
        }

        switchesPositions = new ArrayList<>();

        // Switches for Landing gears
        switchesPositions.add(new Vector2(735, 343));
        switchesPositions.add(new Vector2(735, 236));
        switchesPositions.add(new Vector2(735, 126));

        // Switches for Flaps
        switchesPositions.add(new Vector2(1142, 342));
        switchesPositions.add(new Vector2(1142, 235));
        switchesPositions.add(new Vector2(1142, 126));
        switchesPositions.add(new Vector2(1142, 18));

        // Switches for Brakes
        switchesPositions.add(new Vector2(867, 100));
        switchesPositions.add(new Vector2(940, 100));
        switchesPositions.add(new Vector2(1012, 100));

        // Blue Markers
        blueMarkerPosition = new Vector2(865, 368);

        // Red Markers
        redMarkerPosition = new Vector2(864, 235);

        // Orange Markers
        orangeMarkerPosition = new Vector2(1010, 349);

        // Coffee positions list
        coffeesPositions = new ArrayList<>();
        coffeesPositions.add(new Vector2(759, 66));
        coffeesPositions.add(new Vector2(760, 21));
        coffeesPositions.add(new Vector2(809, 21));

    }

    public void drawDice(ArrayList<Dice> diceList, ArrayList<Texture> diceTextures, boolean isPilot) {

        for (int i = 0; i < diceList.size(); i++) {
            final Dice dice = diceList.get(i);
            final Image diceImage = new Image(diceTextures.get(dice.getDiceValue() - 1));

            diceImage.setPosition(isPilot ? pilotStartX + i * diceSpacing : coPilotStartX + i * diceSpacing,
                    isPilot ? pilotStartY : coPilotStartY);
            diceImage.setSize(51, 51);

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
                if (gameLogic.getRound().getCurrentPlayerIndex() == 0) {
                    System.out.println("Pilot Dice touchDown");
                    isDraggingPilotDice = true;
                    draggedPilotDice = dice;
                    dragStartX = x;
                    dragStartY = y;
                    return true;
                }
                return false;

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
                if (gameLogic.getRound().getCurrentPlayerIndex() == 1) {
                    System.out.println("Co-Pilot Dice touchDown");
                    isDraggingCoPilotDice = true;
                    draggedCoPilotDice = dice;
                    dragStartX = x;
                    dragStartY = y;
                    return true;
                }
                return false;
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
        // Drawing planeTrack and planes before board, so when they're updated with the
        // logic, they move under the board, not above
        updatePlaneTrackVerticalOffset(gameLogic.getAirplane().getEngine().getCurrentPosition());
        drawBoardAndAltitudeTrack();
        drawSwitchesAndMarkers();
        axisSprite.draw(batch);

        playerIndex = gameLogic.getRound().getCurrentPlayerIndex();

        if (playerIndex == 0) {
            batch.draw(pilotTurnTexture, 300, 750);
        } else if (playerIndex == 1) {
            batch.draw(coPilotTurnTexture, Gdx.graphics.getWidth() - 600, 750);
        }

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    private void drawBoardAndAltitudeTrack() {
        float scaleFactor = 0.8f;

        float altitudeTrackWidth = altitudeTrack.getWidth() * scaleFactor - 6;
        float altitudeTrackHeight = altitudeTrack.getHeight() * scaleFactor;
        float altitudeTrackX = (Gdx.graphics.getWidth() - altitudeTrackWidth) / 2f + 95 - 2;
        float altitudeTrackY = boardTexture.getHeight() * scaleFactor - 100;
        batch.draw(altitudeTrack, altitudeTrackX, altitudeTrackY, altitudeTrackWidth, altitudeTrackHeight);

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

        // Draw Switches
        for (int i = 0; i < switchesPositions.size(); i++) {
            Vector2 position = switchesPositions.get(i);
            boolean isSwitchOn = switchesStates.get(i);

            float switchX = isSwitchOn ? position.x - 5 : position.x + 20;
            float switchY = position.y;
            float switchSize = 28;

            batch.draw(switchTrack, switchX, switchY, switchSize, switchSize);
        }
        // Draw blue marker
        batch.draw(blueMarkerTrack, blueMarkerPosition.x, blueMarkerPosition.y,
                17 / 2f, 28 / 2f, 17, 28, 1, 1, blueMarkerRotation,
                0, 0, blueMarkerTrack.getWidth(), blueMarkerTrack.getHeight(),
                false, false);
        // Draw red marker
        batch.draw(redMarkerTrack, redMarkerPosition.x, redMarkerPosition.y, 17 / 2f, 28 / 2f, 17, 28, 1, 1,
                redMarkerRotation, 0, 0,
                redMarkerTrack.getWidth(), redMarkerTrack.getHeight(), false, false);
        // Draw orange marker
        batch.draw(orangeMarkerTrack, orangeMarkerPosition.x, orangeMarkerPosition.y,
                17 / 2f, 28 / 2f, 17, 28, 1, 1, orangeMarkerRotation,
                0, 0, orangeMarkerTrack.getWidth(), orangeMarkerTrack.getHeight(),
                false, false);

        // Draw coffee
        for (int i = 0; i < currentCoffeeQuantity; i++) {
            Vector2 position = coffeesPositions.get(i);

            batch.draw(coffeeTrack, position.x, position.y, 36, 35);

        }

    }

    /**
     * Dynamically generates and draws planes based on the number of planes at each
     * track position.
     * 
     * The function adapts to changes in the number of planes at each track
     * position by adjusting the Y-axis for track indices and the X-axis for
     * multiple
     * planes at the same position.
     * 
     * The `planesOnTrack` variable, updated by game logic, automatically reflects
     * the
     * current plane state, removing planes when the `useRadio` event is triggered.
     * 
     * @param planesOnTrack A list representing the number of planes at each track
     *                      position.
     *                      The list size determines the number of positions, and
     *                      each element
     *                      represents how many planes are at that position.
     */
    public void generateAndDrawPlanesWithTrack(ArrayList<Integer> planesOnTrack) {
        planePositions = new ArrayList<>();

        float scaleFactor = 0.26f;
        float scaledWidth = planeTexture.getWidth() * scaleFactor;
        float scaledHeight = planeTexture.getHeight() * scaleFactor;

        // Drawing plane track
        float planeTrackScaleFactor = 0.8f;
        float planeTrackWidth = planeTrack.getWidth() * planeTrackScaleFactor - 6;
        float planeTrackHeight = planeTrack.getHeight() * planeTrackScaleFactor;
        float planeTrackX = (Gdx.graphics.getWidth() - planeTrackWidth) / 2f - 95 + 3;
        float planeTrackY = boardTexture.getHeight() * planeTrackScaleFactor - 100 - planeTrackVerticalOffset;
        batch.draw(planeTrack, planeTrackX, planeTrackY, planeTrackWidth, planeTrackHeight);

        int baseY = 620;
        int verticalSpacing = 75;
        int positionSpacing = 50;

        int screenCenterX = Gdx.graphics.getWidth() / 2;
        int xOffsetFromCenter = -100;
        int xPosition = screenCenterX + xOffsetFromCenter;

        for (int i = 0; i < planesOnTrack.size(); i++) {
            int planesAtCurrentPosition = planesOnTrack.get(i);
            int yBasePosition = baseY + i * verticalSpacing - (int) planeTrackVerticalOffset;

            for (int j = 0; j < planesAtCurrentPosition; j++) {

                int xOffset = 0;
                if (planesAtCurrentPosition == 2) {
                    xOffset = (j == 0) ? -20 : 20;
                } else if (planesAtCurrentPosition == 3) {
                    xOffset = (j == 0) ? -30 : (j == 1 ? 0 : 30);
                }

                int xAdjustedPosition = xPosition + xOffset;
                int yPosition = yBasePosition;

                Vector2 planePosition = new Vector2(xAdjustedPosition, yPosition);
                planePositions.add(planePosition);

                batch.draw(planeTexture, planePosition.x, planePosition.y, scaledWidth, scaledHeight);
            }
        }
    }

    public void updatePlaneTrackVerticalOffset(int currentPosition) {
        // Calculate the planeTrackVerticalOffset based on the currentPosition
        planeTrackVerticalOffset = currentPosition * 80;

        // Call the plane generation and drawing method with updated position
        generateAndDrawPlanesWithTrack(gameLogic.getAirplane().getPlanesOnTrack());
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
                        switchesStates.set(i + 7, true);
                        currentRedMarkerSteps++;
                        if (currentRedMarkerSteps == 1) {
                            redMarkerPosition.set(900, 213);
                            redMarkerRotation = -20;
                        } else if (currentRedMarkerSteps == 2) {
                            redMarkerPosition.set(975, 205);
                            redMarkerRotation = 10;
                        } else if (currentRedMarkerSteps == 3) {
                            redMarkerPosition.set(1044, 240);
                            redMarkerRotation = 40;
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
                        switchesStates.set(i, true);
                        currentBlueMarkerSteps++;
                        if (currentBlueMarkerSteps == 1) {
                            blueMarkerPosition.set(900, 345);
                            blueMarkerRotation = -30;
                        } else if (currentBlueMarkerSteps == 2) {
                            blueMarkerPosition.set(935, 336);
                            blueMarkerRotation = -10;
                        } else if (currentBlueMarkerSteps == 3) {
                            blueMarkerPosition.set(975, 340);
                            blueMarkerRotation = 5;
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
                        switchesStates.set(i + 3, true);
                        currentOrangeMarkerSteps++;
                        if (currentOrangeMarkerSteps == 1) {
                            orangeMarkerPosition.set(1044, 370);
                            orangeMarkerRotation = 40;
                        } else if (currentOrangeMarkerSteps == 2) {
                            orangeMarkerPosition.set(1070, 400);
                            orangeMarkerRotation = 60;
                        } else if (currentOrangeMarkerSteps == 3) {
                            orangeMarkerPosition.set(1086, 433);
                            orangeMarkerRotation = 75;
                        } else if (currentOrangeMarkerSteps == 4) {
                            orangeMarkerPosition.set(1091, 475);
                            orangeMarkerRotation = 90;
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
