package com.mygdx.skyteam.logic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Color;

import java.util.List;

public class Field {
    private String name;
    private List<Integer> validDiceValues;
    private boolean isFilled;
    private Integer placedDice;

    // positions for board UI
    private float x;
    private float y;

    private boolean isHighlighted = false;
    private Rectangle fieldRect;
    private Color color = new Color(0, 0, 0, 0);


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
    public Integer setPlacedDice(int x) {return placedDice = x;}

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

    public void draw(int diceValue) {
        if (canAcceptDice(diceValue)) {
            float width = 50;
            float height = 50;


            if (isHighlighted) {
                color = new Color(0x39 / 255f, 0xFF / 255f, 0x14 / 255f, 1f);
            } else {
                color = new Color(0, 0, 0, 0);
            }

            this.fieldRect = new Rectangle(x, y, width, height);
        }
    }


    public boolean canAcceptDice(int diceValue) {
        return validDiceValues.contains(diceValue) && !isFilled;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public Color getColor() {
        return color;
    }


    public boolean isBrakes() {
        return name.toLowerCase().contains("brake");
    }

    public boolean isFlaps() {
        return name.toLowerCase().contains("flap");
    }
}
