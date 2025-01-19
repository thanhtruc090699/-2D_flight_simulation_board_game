package com.mygdx.skyteam.logic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Color;

import java.util.List;
/**
 * Represents a placeholder on the game board where dice can be placed.
 * Tracks its position, valid dice values, and current state.
 */
public class Field {
    private String name;
    private List<Integer> validDiceValues;
    private boolean isFilled;
    private Integer placedDice;

    // x, y and variables under are used for UI purposes and does not contribute to the core logic.
    private float x;
    private float y;
    private boolean isHighlighted = false;
    private Rectangle fieldRect;
    private Color color = new Color(0, 0, 0, 0);

    /**
     * Creates a new Field with specified properties.
     *
     * @param name            the name of the field.
     * @param validDiceValues a list of valid dice values for the field.
     * @param x               the x-coordinate of the field.
     * @param y               the y-coordinate of the field.
     * Written by: Marija
     */
    public Field(String name, List<Integer> validDiceValues, int x, int y) {
        this.name = name;
        this.validDiceValues = validDiceValues;
        this.isFilled = false;
        this.placedDice = null;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets a dice value on the field if it is valid.
     *
     * @param diceValue the dice value to set.
     * @return true if the value was successfully placed, false otherwise.
     * Written by: Marija
     */
    public boolean setDiceValue(int diceValue) {
        if (validDiceValues.contains(diceValue)) {
            this.placedDice = diceValue;
            this.isFilled = true;
            return true;
        }
        return false;
    }

    /**
     * Resets the field, clearing any placed dice.
     * Written by: Marija
     */
    public void resetField() {
        this.placedDice = null;
        this.isFilled = false;
    }

    /**
     * Checks if the field is currently filled.
     *
     * @return true if the field is filled, false otherwise.
     * Written by: Marija
     */
    public boolean isFilled() {
        return isFilled;
    }

    /**
     * Gets the dice value currently placed on the field.
     *
     * @return the placed dice value, or null if none.
     * Written by: Marija
     */
    public Integer getPlacedDice() {
        return placedDice;
    }

    /**
     * Sets the dice value.
     *
     * @param x the dice value to set.
     * @return the updated dice value.
     * Written by: Marija
     */
    public Integer setPlacedDice(int x) {return placedDice = x;}

    /**
     * Gets the name of the field.
     *
     * @return the field's name.
     * Written by: Marija
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of valid dice values for the field.
     *
     * @return the list of valid dice values.
     * Written by: Marija
     */
    public List<Integer> getValidDiceValues() {
        return validDiceValues;
    }

    /**
     * Sets whether the field is filled.
     *
     * @param filled true to mark the field as filled, false otherwise.
     * Written by: Marija
     */
    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Checks if the mouse is over the field.
     *
     * @param mouseX the x-coordinate of the mouse.
     * @param mouseY the y-coordinate of the mouse.
     * @return true if the mouse is over the field, false otherwise.
     * Written by: Marija
     */
    public boolean isMouseOver(float mouseX, float mouseY) {

        float width = 50;
        float height = 50;
        return mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
    }

    /**
     * Gets the x-coordinate of the field.
     *
     * @return the x-coordinate.
     * Written by: Marija
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the field.
     *
     * @return the y-coordinate.
     * Written by: Marija
     */
    public float getY() {
        return y;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Converts the field's coordinates to stage coordinates.
     *
     * @param stage the game stage.
     * @return the field's coordinates in the stage.
     * Written by: Marija
     */
    public Vector3 getStageCoordinates(Stage stage) {
        Vector3 screenCoordinates = new Vector3(x, y, 0);
        stage.getCamera().unproject(screenCoordinates);
        return screenCoordinates;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Draws the field on the board.
     *
     * @param diceValue the dice value to check for drawing highlights.
     * Written by: Marija
     */
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


    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Checks if the field can accept a specific dice value.
     *
     * @param diceValue the dice value to check.
     * @return true if the field can accept the dice, false otherwise.
     * Written by: Marija
     */
    public boolean canAcceptDice(int diceValue) {
        return validDiceValues.contains(diceValue) && !isFilled;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Sets whether the field is highlighted.
     *
     * @param isHighlighted true to highlight the field, false otherwise.
     * Written by: Marija
     */
    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Checks if the field is currently highlighted.
     *
     * @return true if the field is highlighted, false otherwise.
     * Written by: Marija
     */
    public boolean isHighlighted() {
        return isHighlighted;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Gets the current color of the field.
     *
     * @return the field's color.
     * Written by: Marija
     */
    public Color getColor() {
        return color;
    }


    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Checks if the field is a brake field based on its name.
     *
     * @return true if the field is a brake field, false otherwise.
     * Written by: Marija
     */
    public boolean isBrakes() {
        return name.toLowerCase().contains("brake");
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic.
     * Checks if the field is a flap field based on its name.
     *
     * @return true if the field is a flap field, false otherwise.
     * Written by: Marija
     */
    public boolean isFlaps() {
        return name.toLowerCase().contains("flap");
    }
}
