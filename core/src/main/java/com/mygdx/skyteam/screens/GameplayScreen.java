package com.mygdx.skyteam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;



public class GameplayScreen implements Screen {

    private Stage stage;
    private SkyTeamGame game;
    private Texture boardTexture;  
    private Texture planeTrack;  
    private Texture altitudeTrack;
    private Texture diceTexture;
    private Image diceImage;
    private SpriteBatch batch;

    private float originalX, originalY;
    private float dragOffsetX, dragOffsetY;

    public GameplayScreen(SkyTeamGame game){
        this.game = game;
    }

    @Override
    public void show() {
        boardTexture = new Texture(Gdx.files.internal("board/Control Panel.png"));
        planeTrack = new Texture(Gdx.files.internal("board/Track.png"));
        altitudeTrack = new Texture(Gdx.files.internal("board/Altitude.png"));
        diceTexture = new Texture(Gdx.files.internal("dice/3B.png"));

        batch =  new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);  // Allow user input for the game screen

        diceImage = new Image(diceTexture);
        diceImage.setSize(50, 50);  
        diceImage.setPosition(100, 100);
        originalX = diceImage.getX();     
        originalY = diceImage.getY();  
        stage.addActor(diceImage);

        diceImage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Calculate the offset when the drag starts
                dragOffsetX = x;
                dragOffsetY = y;
                super.touchDown(event, x, y, pointer, button);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                // Update dice position based on drag
                Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
    
                // Update the dice position using stage coordinates
                diceImage.setPosition(stageCoords.x - diceImage.getWidth() / 2, stageCoords.y - diceImage.getHeight() / 2);

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Drop logic: check if the dice is near the valid drop position

                Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

                    // Target drop position (screen coordinates)
                    float targetX = 737;
                    float targetY = 756;

                    // Convert target drop position to stage coordinates
                    Vector2 targetStageCoords = stage.screenToStageCoordinates(new Vector2(targetX, targetY));

                if (isNearDropArea(stageCoords.x, stageCoords.y)) {
                    // If near the valid drop area, snap to drop position
                    System.out.println("Dice dropped in target area, snapping to drop position.");
                    diceImage.setPosition(targetStageCoords.x - diceImage.getWidth()+47, targetStageCoords.y - diceImage.getHeight()); // Drop location (starting position)
                } else {
                    // Return to original position
                     System.out.println("Dice not near target, returning to original position.");
                    diceImage.setPosition(originalX, originalY);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(diceImage);
    }

    private boolean isNearDropArea(float x, float y) {
        // Check if the dice is near the drop area (737, 757)

        float stageX = stage.getCamera().unproject(new com.badlogic.gdx.math.Vector3(x, y, 0)).x;
        float stageY = stage.getCamera().unproject(new com.badlogic.gdx.math.Vector3(x, y, 0)).y;
        float targetX = 737;
        float targetY = 756;

        float distanceX = Math.abs(stageX - targetX);
        float distanceY = Math.abs(stageY - targetY);
        System.out.println("Checking drop area: DistanceX: " + distanceX + ", DistanceY: " + distanceY); 
        return distanceX < 50 && distanceY < 50; // Tolerate 56px distance for a drop
    }


    @Override
    public void render(float delta) {

        float screenX = Gdx.input.getX();
        float screenY = Gdx.input.getY();

    // Print the X and Y position of the input on the screen
        System.out.println("Screen Position - X: " + screenX + ", Y: " + screenY);
   

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen

        batch.begin();
        float scaleFactor = 0.8f;

        float planeTrackWidth = planeTrack.getWidth() * scaleFactor;
        float planeTrackHeight = planeTrack.getHeight() * scaleFactor;
        float planeTrackX = (Gdx.graphics.getWidth() - planeTrackWidth) / 2f - 95; 
        float planeTrackY = boardTexture.getHeight() * scaleFactor-100; 
        batch.draw(planeTrack, planeTrackX, planeTrackY, planeTrackWidth, planeTrackHeight);

    // Resize and draw the altitude track
        float altitudeTrackWidth = altitudeTrack.getWidth() * scaleFactor;
        float altitudeTrackHeight = altitudeTrack.getHeight() * scaleFactor;
        float altitudeTrackX = (Gdx.graphics.getWidth() - altitudeTrackWidth) / 2f + 95; 
        batch.draw(altitudeTrack, altitudeTrackX, planeTrackY, altitudeTrackWidth, altitudeTrackHeight);

    // Resize and draw the board texture
        float boardWidth = boardTexture.getWidth() * scaleFactor;
        float boardHeight = boardTexture.getHeight() * scaleFactor;
        float boardX = (Gdx.graphics.getWidth() - boardWidth) / 2f;  
        float boardY = 0;  
        batch.draw(boardTexture, boardX, boardY, boardWidth, boardHeight);

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Act stage (update)
        stage.draw();  // Draw stage (render game components)

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);  // Update stage on resize
    }

    @Override
    public void hide() {
        dispose();  // Dispose of stage when hidden
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();  
        if (boardTexture != null) boardTexture.dispose();  
        if (batch != null) batch.dispose(); 
    }
}
