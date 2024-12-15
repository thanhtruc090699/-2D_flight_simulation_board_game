package com.mygdx.skyteam.logic;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

public class Field {
    private String name;
    private List<Integer> validDiceValues;
    private boolean isFilled;
    private Integer placedDice;

    // positions for board UI
    private float x;
    private float y;


    public Field(String name, List<Integer> validDiceValues, int x, int y) {
        this.name = name;
        this.validDiceValues = validDiceValues;
        this.isFilled = false;
        this.placedDice = null;
        this.x = x;
        this.y = y;
    }

    public boolean setDiceValue(int diceValue) {
        if (validDiceValues.contains(diceValue)) {
            this.placedDice = diceValue;
            this.isFilled = true;
            return true;
        }
        return false;
    }

    public void resetField() {
        this.placedDice = null;
        this.isFilled = false;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public Integer getPlacedDice() {
        return placedDice;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getValidDiceValues() {
        return validDiceValues;
    }

    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    public boolean isMouseOver(float mouseX, float mouseY) {

        float width = 50;
        float height = 50;
        return mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector3 getStageCoordinates(Stage stage) {
        Vector3 screenCoordinates = new Vector3(x, y, 0);
        stage.getCamera().unproject(screenCoordinates);
        return screenCoordinates;
    }

}
