package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Manages the airplane's braking system.
 * Handles brake deployment based on pilot input and ensures correct deployment order.
 */
public class Brake {
    private int redMarker;
    private ArrayList<Field> brakeFields;

    /**
     * Initializes the Brake system with default fields and a red marker at 0.
     *
     * Written by: Rathin
     */
    public Brake() {
        redMarker = 0;
        brakeFields = new ArrayList<>();

        brakeFields.add(new Field("Brake Field 1", Arrays.asList(2), 862, 890));
        brakeFields.add(new Field("Brake Field 2", Arrays.asList(4), 936, 890));
        brakeFields.add(new Field("Brake Field 3", Arrays.asList(6), 1012, 890));
    }

    /**
     * Deploys the brakes based on pilot input.
     * Ensures deployment order: Brake Field 1 -> Brake Field 2 -> Brake Field 3.
     *
     * @param pilotInput the dice value (2, 4, or 6) representing the brake to deploy.
     *
     * Written by: Rathin
     */
    public void deployBrakes(int pilotInput) {
        if (pilotInput == 2) {
            if (!brakeFields.get(0).isFilled()) {
                brakeFields.get(0).setDiceValue(pilotInput);
                redMarker = pilotInput;
                /* System.out.println("Brake field 1 is now deployed."); */
            } else {
               /*  System.out.println("Brake field 1 is already occupied."); */
            }
        } else if (pilotInput == 4) {
            if (!brakeFields.get(0).isFilled()) {
                /* System.out.println("Brake field 1 must be deployed first before deploying brake field 2."); */
            } else if (!brakeFields.get(1).isFilled()) {
                brakeFields.get(1).setDiceValue(pilotInput);
                redMarker = pilotInput;
               /*  System.out.println("Brake field 2 is now deployed."); */
            } else {
                /* System.out.println("Brake field 2 is already occupied."); */
            }
        } else if (pilotInput == 6) {
            if (!brakeFields.get(0).isFilled() || !brakeFields.get(1).isFilled()) {
                /* System.out.println(
                        "Both Brake field 1 and Brake field 2 must be deployed first before deploying brake field 3."); */
            } else if (!brakeFields.get(2).isFilled()) {
                brakeFields.get(2).setDiceValue(pilotInput);
                redMarker = pilotInput;
                /* System.out.println("Brake field 3 is now deployed."); */
            } else {
                /* System.out.println("Brake field 3 is already occupied."); */
            }
        } else {
            /* System.out.println("Invalid input for brake deployment. Please choose from 2, 4, or 6."); */
        }
    }

    /**
     * Checks if brakes can be placed based on the pilot input.
     *
     * @param pilotInput the dice value to check (2, 4, or 6).
     * @return true if brakes can be placed, false otherwise.
     */
    public boolean canPlaceBrakes(int pilotInput) {
        if (pilotInput != 2 && pilotInput != 4 && pilotInput != 6) {
            /* System.out.println("Invalid input for brake deployment. Please choose from 2, 4, or 6."); */
            return false;
        }

        if (pilotInput == 2) {
            if (brakeFields.get(0).isFilled()) {
                /* System.out.println("Brake field 1 is already occupied."); */
                return false;
            }
        }

        else if (pilotInput == 4) {
            if (!brakeFields.get(0).isFilled()) {
                /* System.out.println("Brake field 1 must be deployed first before deploying brake field 2."); */
                return false;
            } else if (brakeFields.get(1).isFilled()) {
                /* System.out.println("Brake field 2 is already occupied."); */
                return false;
            }
        }

        else if (pilotInput == 6) {
            if (!brakeFields.get(0).isFilled() || !brakeFields.get(1).isFilled()) {
                /* System.out.println(
                        "Both Brake field 1 and Brake field 2 must be deployed first before deploying brake field 3."); */
                return false;
            } else if (brakeFields.get(2).isFilled()) {
                /* System.out.println("Brake field 3 is already occupied."); */
                return false;
            }
        }

        return true;
    }

    /**
     * Sets the red marker to a specific value.
     *
     * @param redMarker the new red marker value.
     * @throws IllegalArgumentException if the value is invalid.
     *
     * Written by: Rathin
     */
    public void setRedMarker(int redMarker) {
        if (redMarker > 1 && redMarker < 7) {
            this.redMarker = redMarker;
        } else {
            throw new IllegalArgumentException("Not a valid input");
        }
    }

    /**
     * Gets the list of brake fields.
     *
     * @return the list of brake fields.
     */
    public ArrayList<Field> getBrakeFields() {
        return brakeFields;
    }


    /**
     * Gets the current red marker value.
     *
     * @return the red marker value.
     */
    public int getRedMarker() {
        return redMarker;
    }

    /**
     * This function is primarily used for UI purposes and does not contribute to the core logic!
     * Checks if brakes can be placed for a specific dice.
     *
     * @param diceValue the dice value to check (2, 4, or 6).
     * @return true if brakes can be placed, false otherwise.
     *
     * Written by: Rathin
     */
    public boolean canPlaceBrakesForDice(int diceValue) {
        if (diceValue != 2 && diceValue != 4 && diceValue != 6) {
            return false;
        }

        if (diceValue == 2) {
            if (brakeFields.get(0).isFilled()) {
                return false;
            }
        } else if (diceValue == 4) {
            if (!brakeFields.get(0).isFilled()) {
                return false;
            } else if (brakeFields.get(1).isFilled()) {
                return false;
            }
        } else if (diceValue == 6) {
            if (!brakeFields.get(0).isFilled() || !brakeFields.get(1).isFilled()) {
                return false;
            } else if (brakeFields.get(2).isFilled()) {
                return false;
            }
        }

        return true;
    }
}
