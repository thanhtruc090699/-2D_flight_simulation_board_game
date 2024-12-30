package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.mygdx.skyteam.logic.Pilot;

public class GameplayScreen implements Screen {

    private Stage stage;
    private SkyTeamGame game;
    private GameLogic gameLogic;
    private Texture boardTexture, planeTrack, altitudeTrack, axisIcon, backgroundTexture;
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
    private boolean isTurnChanged = true;
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

    private Texture[] roundTextures;
    private int previousRoundNumber = 1;

    private int lastKnownAltitude = 6000;
    private float altitudeOffset = 0;

    private Texture buttonUpTexture;
    private Texture buttonDownTexture;
    private ArrayList<Vector2> buttonUpPilotPositions;
    private ArrayList<Vector2> buttonDownPilotPositions;
    private ArrayList<Vector2> buttonUpCoPilotPositions;
    private ArrayList<Vector2> buttonDownCoPilotPositions;
    private  Boolean isUsingCoffee=false;
    private ArrayList<Boolean> coffeePilotDiceSelectable;
    private ArrayList<Boolean> coffeeCopilotDiceSelectable;
    private  ArrayList<Boolean> PilotDicePlacedInPlaceHolder;
    private  ArrayList<Boolean> CopilotDicePlacedInPlaceHolder;



    public GameplayScreen(SkyTeamGame game, GameLogic gameLogic) {
        this.game = game;
        this.gameLogic = gameLogic;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("images/wood_bg.jpg"));
        boardTexture = new Texture(Gdx.files.internal("board/Control Panel.png"));
        planeTrack = new Texture(Gdx.files.internal("board/Track.png"));
        altitudeTrack = new Texture(Gdx.files.internal("board/Altitude.png"));
        axisIcon = new Texture(Gdx.files.internal("board/axis_icon.png"));
        switchTrack = new Texture(Gdx.files.internal("board/icons/Switch.png"));
        blueMarkerTrack = new Texture(Gdx.files.internal("board/markers/MarkerBlue.png"));
        redMarkerTrack = new Texture(Gdx.files.internal("board/markers/MarkerRed.png"));
        orangeMarkerTrack = new Texture(Gdx.files.internal("board/markers/MarkerOrange.png"));
        coffeeTrack = new Texture(Gdx.files.internal("board/icons/Coffee.png"));
        buttonDownTexture = new Texture(Gdx.files.internal("buttons/1832043-200.png"));
        buttonUpTexture = new Texture(Gdx.files.internal("buttons/1832044-200.png"));
        pilotTurnTexture = new Texture(Gdx.files.internal("images/pilots_turn.png"));
        coPilotTurnTexture = new Texture(Gdx.files.internal("images/copilots_turn.png"));
        axisSprite = new Sprite(axisIcon);
        planeTexture = new Texture(Gdx.files.internal("board/icons/Plane.png"));
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        roundTextures = new Texture[7];

        Gdx.input.setInputProcessor(stage);

        pilotDiceTextures = new ArrayList<>();
        coPilotDiceTextures = new ArrayList<>();





        for (int i = 1; i <= 6; i++) {
            pilotDiceTextures.add(new Texture("dice/" + i + "B.png"));
            coPilotDiceTextures.add(new Texture("dice/" + i + "R.png"));
        }

        for (int i = 0; i < 7; i++) {
            roundTextures[i] = new Texture(Gdx.files.internal("images/round" + (i + 1) + ".png"));
        }

        gameLogic.startGame();

        pilotDice = new ArrayList<>(gameLogic.getPilot().getDices());
        coPilotDice = new ArrayList<>(gameLogic.getCoPilot().getDices());



        drawDice(pilotDice, pilotDiceTextures, true);
        drawDice(coPilotDice, coPilotDiceTextures, false);

        switchesPositions = new ArrayList<>();
        switchesStates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            switchesStates.add(false);
        }

        switchesPositions = new ArrayList<>();

        // Switches for Landing gears
        switchesPositions.add(new Vector2(735, 343));
        switchesPositions.add(new Vector2(735, 235));
        switchesPositions.add(new Vector2(735, 126));

        // Switches for Flaps
        switchesPositions.add(new Vector2(1142, 343));
        switchesPositions.add(new Vector2(1142, 235));
        switchesPositions.add(new Vector2(1142, 126));
        switchesPositions.add(new Vector2(1142, 18));

        // Switches for Brakes
        switchesPositions.add(new Vector2(867, 100));
        switchesPositions.add(new Vector2(940, 100));
        switchesPositions.add(new Vector2(1013, 100));

        // Blue Markers
        blueMarkerPosition = new Vector2(865, 367);

        // Red Markers
        redMarkerPosition = new Vector2(864, 235);

        // Orange Markers
        orangeMarkerPosition = new Vector2(1010, 349);

        // Coffee positions list
        coffeesPositions = new ArrayList<>();
        coffeesPositions.add(new Vector2(760, 66));
        coffeesPositions.add(new Vector2(760, 21));
        coffeesPositions.add(new Vector2(809, 21));



        buttonUpPilotPositions = new ArrayList<>();
        buttonDownPilotPositions = new ArrayList<>();
        buttonUpCoPilotPositions = new ArrayList<>();
        buttonDownCoPilotPositions = new ArrayList<>();

        isUsingCoffee = false;

        coffeePilotDiceSelectable = new ArrayList<>(); //Pilot Selectable dice list
        coffeeCopilotDiceSelectable = new ArrayList<>(); // Copilot Selectable dice list
        PilotDicePlacedInPlaceHolder = new ArrayList<>(); // Pilot dice list of placed dice
        CopilotDicePlacedInPlaceHolder = new ArrayList<>();  // CoPilot dice list of placed dice


        // Position of increase and decrease button of pilot
        for(int i=0;i<pilotDice.size();i++){
            Dice dice = pilotDice.get(i);
            coffeePilotDiceSelectable.add(true);
            PilotDicePlacedInPlaceHolder.add(false);
            float xPosition = pilotStartX + i * diceSpacing;
            float yPosition = pilotStartY;
            Vector2 buttonUpPosition = new Vector2(xPosition , yPosition - 50);
            Vector2 buttonDownPosition = new Vector2(xPosition , yPosition + 50);

            buttonUpPilotPositions.add(buttonUpPosition);
            buttonDownPilotPositions.add(buttonDownPosition);


        }

        // Position of increase and decrease button of Copilot

        for(int i=0;i<coPilotDice.size();i++){
            Dice dice = coPilotDice.get(i);
            coffeeCopilotDiceSelectable.add(true);
            CopilotDicePlacedInPlaceHolder.add(false);
            float xPosition = coPilotStartX + i * diceSpacing;
            float yPosition = coPilotStartY;
            Vector2 buttonUpPosition = new Vector2(xPosition , yPosition - 50);
            Vector2 buttonDownPosition = new Vector2(xPosition , yPosition + 50);

            buttonUpCoPilotPositions.add(buttonUpPosition);
            buttonDownCoPilotPositions.add(buttonDownPosition);


        }
        handlePilotCoffeeInteraction();
        handleCoPilotCoffeeInteraction();




    }

    public void drawDice(ArrayList<Dice> diceList, ArrayList<Texture> diceTextures, boolean isPilot) {



        for (int i = 0; i < diceList.size(); i++) {


            final Dice dice = diceList.get(i);
            final Image diceImage = new Image(diceTextures.get(dice.getDiceValue() - 1));

            diceImage.setName(isPilot ? "PilotDice" : "CoPilotDice");

            float xPosition = isPilot ? pilotStartX + i * diceSpacing : coPilotStartX + i * diceSpacing;
            float yPosition = isPilot ? pilotStartY : coPilotStartY;

            diceImage.setPosition(xPosition, yPosition);
            diceImage.setSize(51, 51);

            stage.addActor(diceImage);



            if (isPilot) {
                diceImage.setZIndex(10); // Set a higher z-index for pilot dice
            }

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

    /**
     * Resets and redraws the dice for either the pilot or co-pilot.
     * - Removes existing dice images from the stage.
     * - Clears any dragging state for the corresponding dice.
     * - Redraws the dice in their original positions.
     *
     * @param diceList     The list of dice objects to be drawn.
     * @param diceTextures The list of textures corresponding to the dice values.
     * @param isPilot      A boolean indicating whether the dice are for the pilot
     *                     (true) or co-pilot (false).
     */
    public void resetAndDrawDice(ArrayList<Dice> diceList, ArrayList<Texture> diceTextures, boolean isPilot) {

        ArrayList<Actor> actorsToRemove = new ArrayList<>();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Image) {
                Image image = (Image) actor;

                if ((image.getName().equals("PilotDice") && isPilot)
                        || (image.getName().equals("CoPilotDice") && !isPilot)) {
                    actorsToRemove.add(image);
                }
            }
        }
        for (Actor actor : actorsToRemove) {
            actor.remove();
        }


        if (isPilot) {
            isDraggingPilotDice = false;

        } else {
            isDraggingCoPilotDice = false;
        }
        drawDice(diceList, diceTextures, isPilot);
    }

    /**
     * Update the single dice image after the player (Copilot or Pilot) chooses to increase or decrease dice when using coffee token
     * It refreshes the texture of dice ti match its value and repositions it at its original location
     * - Find the specific dice image in the stage by iterating over all actor
     * - Update the texture of image and set its position back to the start position
     * @param diceIndex   The index of the dice need to be updated
     * @param dice        The dice object that holds the updated value
     * @param isPilot     A boolean indicating whether the dice are for the pilot
     *                   (true) or co-pilot (false).
     */
    private void updateSingleDice(int diceIndex, Dice dice, boolean isPilot) {
        ArrayList<Texture> diceTextures = isPilot ? pilotDiceTextures : coPilotDiceTextures;
        float startX = isPilot ? pilotStartX : coPilotStartX;
        float startY = isPilot ? pilotStartY : coPilotStartY;

        int actorIndex = 0;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Image) {
                Image diceImage = (Image) actor;

                if ((isPilot && diceImage.getName().equals("PilotDice")) ||
                    (!isPilot && diceImage.getName().equals("CoPilotDice"))) {

                    if (actorIndex == diceIndex) {
                        diceImage.setDrawable(new Image(diceTextures.get(dice.getDiceValue() - 1)).getDrawable());
                        diceImage.setPosition(startX + diceIndex * diceSpacing, startY);
                        System.out.println("Updated " + (isPilot ? "Pilot" : "CoPilot") +
                            " Dice at index: " + diceIndex +
                            ", Value: " + dice.getDiceValue());
                        return;
                    }
                    actorIndex++;
                }
            }
        }
        System.out.println("Failed to update " + (isPilot ? "Pilot" : "CoPilot") +
            " Dice at index: " + diceIndex);
    }


    /**
     * Handle the input when Pilot player clicks the increase button or decrease button to adjust the dice value that
     * they want after using coffee token
     * - Pilot can select coffee token
     * - The increase and decrease button will appear above (increase button) and below (decrease button) the dice list
     * - Pilot can click the button to increase or decrease the value of a specific dice that they want
     * The Logic of method:
     * 1. Detects a click on a coffee token. if clicked:
     *  - Activate the using coffee mode (isUsingCoffee = true)
     *  - Remove the coffee token from the board
     *  - Update the state of the selectable dice. Selectable dice list is a list of dices that has not been placed
     * 2. Detect a click on increase or decrease button for each dice:
     *  - If increase button is clicked and the dice value is less than 6, it increases the dice value by one
     *  - If decrease button is clicked and the dice value is greater than 1, it decreases the dice value by one
     *  - Updates the image of dice corresponding to the updated value
     *  3. Escape from coffee mode (isUsingCoffee = false) after all
     *
     */
    private  void handlePilotCoffeeInteraction(){
        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(!isUsingCoffee){
                    for(int i =0; i<coffeesPositions.size();i++){
                            Vector2 coffeePosition = coffeesPositions.get(i);
                            if(Math.abs(coffeePosition.x-event.getStageX())<35 &&
                                Math.abs(coffeePosition.y -event.getStageY())<35){
                                isUsingCoffee = true;
                                coffeesPositions.remove(i);
                                currentCoffeeQuantity--;
                                for(int j = 0; j< coffeePilotDiceSelectable.size(); j++){
                                    coffeePilotDiceSelectable.set(j, !PilotDicePlacedInPlaceHolder.get(j));
                                }
                                return true;
                        }

                        }

                    } else {
                        for(int i=0; i< coffeePilotDiceSelectable.size();i++){
                            if(coffeePilotDiceSelectable.get(i) && !PilotDicePlacedInPlaceHolder.get(i)){
                                Vector2 upPos = buttonUpPilotPositions.get(i);
                                Vector2 downPos = buttonDownPilotPositions.get(i);

                                if(Math.abs(upPos.x -event.getStageX()) < 50 &&
                                Math.abs(upPos.y -event.getStageY()) < 50){
                                    Dice dice = pilotDice.get(i);

                                    if(dice.getDiceValue() > 1){
                                        dice.setDiceValue(dice.getDiceValue()-1);
                                        updateSingleDice(i, dice, true);
                                    }


                                    isUsingCoffee=false;
                                    return true;

                                }

                                if(Math.abs(downPos.x -event.getStageX()) < 50 &&
                                Math.abs(downPos.y -event.getStageY()) < 50){
                                    Dice dice = pilotDice.get(i);

                                    if (dice.getDiceValue()<6){
                                        dice.setDiceValue(dice.getDiceValue()+1);
                                        updateSingleDice(i, dice, true);
                                    }
                                    isUsingCoffee=false;
                                    return true;
                                }

                            }
                    }

                }

                return false;
            }
        });
    }

    /**
     * Handle the input when CoPilot player clicks the increase button or decrease button to adjust the dice value that
     * they want after using coffee token
     * - CoPilot can select coffee token
     * - The increase and decrease button will appear above (increase button) and below (decrease button) the dice list
     * - CoPilot can click the button to increase or decrease the value of a specific dice that they want
     * The Logic of method:
     * 1. Detects a click on a coffee token. if clicked:
     *  - Activate the using coffee mode (isUsingCoffee = true)
     *  - Remove the coffee token from the board
     *  - Update the state of the selectable dice. Selectable dice list is a list of dices that has not been placed
     * 2. Detect a click on increase or decrease button for each dice:
     *  - If increase button is clicked and the dice value is less than 6, it increases the dice value by one
     *  - If decrease button is clicked and the dice value is greater than 1, it decreases the dice value by one
     *  - Updates the image of dice corresponding to the updated value
     *  3. Escape from coffee mode (isUsingCoffee = false) after all
     *
     */
    private  void handleCoPilotCoffeeInteraction(){
        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(!isUsingCoffee){
                    for(int i =0; i<coffeesPositions.size();i++){
                        Vector2 coffeePosition = coffeesPositions.get(i);
                        if(Math.abs(coffeePosition.x-event.getStageX())<35 &&
                            Math.abs(coffeePosition.y -event.getStageY())<35){
                            isUsingCoffee = true;
                            coffeesPositions.remove(i);
                            currentCoffeeQuantity--;
                            for(int j = 0; j< coffeeCopilotDiceSelectable.size(); j++){
                                coffeeCopilotDiceSelectable.set(j, !CopilotDicePlacedInPlaceHolder.get(j));
                            }
                            return true;
                        }

                    }

                } else {
                    for(int i=0; i< coffeeCopilotDiceSelectable.size();i++){
                        if(coffeeCopilotDiceSelectable.get(i) && !CopilotDicePlacedInPlaceHolder.get(i)){
                            Vector2 upPos = buttonUpCoPilotPositions.get(i);
                            Vector2 downPos = buttonDownCoPilotPositions.get(i);

                            if(Math.abs(upPos.x -event.getStageX()) < 50 &&
                                Math.abs(upPos.y -event.getStageY()) < 50){
                                Dice dice = coPilotDice.get(i);

                                if(dice.getDiceValue() > 1){
                                    dice.setDiceValue(dice.getDiceValue()-1);
                                    updateSingleDice(i, dice, false);
                                        ;
                                }


                                isUsingCoffee=false;
                                return true;

                            }

                            if(Math.abs(downPos.x -event.getStageX()) < 50 &&
                                Math.abs(downPos.y -event.getStageY()) < 50){
                                Dice dice = coPilotDice.get(i);

                                if (dice.getDiceValue()<6){
                                    dice.setDiceValue(dice.getDiceValue()+1);
                                    updateSingleDice(i, dice, false);
                                }
                                isUsingCoffee=false;
                                return true;
                            }
                            System.out.println("CoPilot Dice " + i + " Selectable: " + coffeeCopilotDiceSelectable.get(i));
                            System.out.println("CoPilot Dice " + i + " Placed: " + CopilotDicePlacedInPlaceHolder.get(i));
                            System.out.println("CoPilot Dice " + i + " Value: " + coPilotDice.get(i).getDiceValue());

                        }
                    }

                }

                return false;
            }
        });
    }

    public void handleRoundChange() {
        // Check if the round number has changed
        int currentRoundNumber = gameLogic.getCurrentRoundNumber();
        playerIndex = gameLogic.getRound().getCurrentPlayerIndex();
        if (currentRoundNumber != previousRoundNumber) {
            previousRoundNumber = currentRoundNumber;


            if (playerIndex==0) resetAndDrawDice(pilotDice, pilotDiceTextures, true);
            else if(playerIndex==1) resetAndDrawDice(coPilotDice, coPilotDiceTextures, false);




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
                if(gameLogic.getRound().getCurrentPlayerIndex()==0){
                    int diceIndex = pilotDice.indexOf(draggedPilotDice);
                    handleInput((int) event.getStageX(), (int) event.getStageY(), true, diceImage, diceIndex);
                } else {
                    System.out.println("Pilot Dice touchUp is ignored: Not Pilot's turn");
                }

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
                if (gameLogic.getRound().getCurrentPlayerIndex() == 1) {
                    int diceIndex = coPilotDice.indexOf(draggedCoPilotDice);
                    handleInput((int) event.getStageX(), (int) event.getStageY(), false, diceImage, diceIndex);
                } else {
                    System.out.println("Co-Pilot Dice touchUp ignored: Not Co-Pilot's turn.");
                }
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

        if (gameLogic.getGameOver()) {
            // Transition to GameOverScreen
            game.setScreen(new GameOverScreen(game));
            return;
        }



        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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


        handleRoundChange();

        renderRoundNumber();
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    private void renderRoundNumber() {
        int currentRound = gameLogic.getCurrentRoundNumber();

        if (currentRound >= 1 && currentRound <= 7) {
            Texture roundTexture = roundTextures[currentRound - 1];

            float xPosition = 30;
            float yPosition = Gdx.graphics.getHeight() - 80;

            batch.draw(roundTexture, xPosition, yPosition);
        }
    }

    private void drawBoardAndAltitudeTrack() {
        float scaleFactor = 0.8f;

        int currentAltitude = gameLogic.getAirplane().getAltitude().getAltitudeValue();

        if (currentAltitude != lastKnownAltitude) {
            altitudeOffset += 75;
            lastKnownAltitude = currentAltitude;

        }

        float altitudeTrackWidth = altitudeTrack.getWidth() * scaleFactor - 6;
        float altitudeTrackHeight = altitudeTrack.getHeight() * scaleFactor;
        float altitudeTrackX = (Gdx.graphics.getWidth() - altitudeTrackWidth) / 2f + 95 - 2;
        float altitudeTrackY = boardTexture.getHeight() * scaleFactor - 100 - altitudeOffset;
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

    /** This method draws:
     *  - Switches on both Landing Gears and Flaps:
     *      + They are drawn by their positions and states
     *      + When the dice is placed into corresponding field of landing gear or flaps,
     *      the associated switches state turn true (on).
     *      + Activated switches automatically move to the left so that the players can visually
     *      see the green light, representing  this flaps or gears has been activated
     *      + The activation states and position switch is preserved throughout 7 rounds
     *
     *  - Red marker on Brakes:
     *      + It is drawn by their position and rotation angles
     *      + Its Positions were already defined above in show method
     *      + When the pilot placed dice, the position of the red marker will change accordingly
     *      to visually show for the player the range of brake level
     *
     *  - Orange and Blue markers on Engine Speed Adjustment:
     *      + It is drawn by their position and rotation angles
     *      + Their Positions were already defined above in show method
     *      + When the players placed dice, the position of the blue or orange marker will change accordingly
     *      to visually show for the player the range of speed engine adjustment
     *  - Coffee token on Concentration mode:
     *      + It is drawn by its respective position and current quantity
     *      + When the player places dice into Concentration field, the coffee icon will appear
     *  - Increase and decrease buttons when using coffee mode is activated
     *      + Buttons appear when Coffee mode (isUsingCoffee) is active and for selectable dice
     *      + These buttons allow pilot or copilot to adjust one dice value
     *      + Only buttons corresponding to unplaced dice
     *      (!PilotDicePlacedPlaceHolder or !CoPilotDicePlacedPlaceHolder) are shown
     *
     */
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

            batch.draw(coffeeTrack, position.x, position.y, 35, 35);

        }
        if(isUsingCoffee){

            for(int i=0; i < coffeePilotDiceSelectable.size();i++ ){
                if(coffeePilotDiceSelectable.get(i) && !PilotDicePlacedInPlaceHolder.get(i)){
                    Vector2 positionButtonUp = buttonUpPilotPositions.get(i);
                    Vector2 positionButtonDown = buttonDownPilotPositions.get(i);
                    batch.draw(buttonUpTexture, positionButtonUp.x, positionButtonUp.y, 50,50);
                    batch.draw(buttonDownTexture, positionButtonDown.x, positionButtonDown.y, 50,50);
                }
                System.out.println("Pilot Dice " + i + " Selectable: " + coffeePilotDiceSelectable.get(i));
                System.out.println("Pilot Dice " + i + " Placed: " + PilotDicePlacedInPlaceHolder.get(i));

            }



            for(int i=0; i < coffeeCopilotDiceSelectable.size();i++ ){
                if(coffeeCopilotDiceSelectable.get(i) && !CopilotDicePlacedInPlaceHolder.get(i)){
                    Vector2 positionButtonUp = buttonUpCoPilotPositions.get(i);
                    Vector2 positionButtonDown = buttonDownCoPilotPositions.get(i);
                    batch.draw(buttonUpTexture, positionButtonUp.x, positionButtonUp.y, 50,50);
                    batch.draw(buttonDownTexture, positionButtonDown.x, positionButtonDown.y, 50,50);
                }
                System.out.println("CoPilot Dice " + i + " Selectable: " + coffeeCopilotDiceSelectable.get(i));
                System.out.println("CoPilot Dice " + i + " Placed: " + CopilotDicePlacedInPlaceHolder.get(i));

            }
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

        planeTrackVerticalOffset = currentPosition * 75;

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
                    PilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                    PilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                        PilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                        PilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                        PilotDicePlacedInPlaceHolder.set(diceIndex,true);
                        switchesStates.set(i, true);
                        currentBlueMarkerSteps++;
                        if (currentBlueMarkerSteps == 1) {
                            blueMarkerPosition.set(900, 344);
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
                    PilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                    CopilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                    CopilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                        CopilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                        CopilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
                        CopilotDicePlacedInPlaceHolder.set(diceIndex,true);
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
