package com.mygdx.skyteam.logic;
/**
 * Manages the airplane's altitude in the game.
 * Provides functionality to adjust altitude based on the current game round and handle the "reroll" token.
 */
public class Altitude{
        private int altitude;
        private Token rerollToken;
        /**
         * Initializes the Altitude object with a starting altitude of 6000 feet and 1 "reroll" token.
         */
        public Altitude(){
            altitude=6000;
            rerollToken=new Token("reroll", 1);
        }
        /**
         * Adjusts the altitude based on the current game round.
         * Adds one more "reroll" tokens at round 5.
         *
         * @param currentRoundNumber the current round number in the game.
         */
        public void adjustAltitude(int currentRoundNumber){
            switch (currentRoundNumber) {
                case 2: setAltitude(5000); break;
                case 3: setAltitude(4000); break;
                case 4: setAltitude(3000); break;
                case 5: setAltitude(2000);
                        rerollToken.setQuantity(rerollToken.getQuantity()+1); break;
                case 6: setAltitude(1000); break;
                case 7: setAltitude(0); break;
                default: break;
            }
        }
        /**
         * Gets the current altitude value.
         *
         * @return the altitude value (unit: feet).
         */
        public int getAltitudeValue(){
            return altitude;
        }
        /**
         * Updates the altitude to a new value.
         * Only accepts values between 0 and 6000 feet.
         *
         * @param altitude the new altitude value.
         * @throws IllegalArgumentException if the altitude exceeds the allowed range.
         */
        public void setAltitude(int altitude){
            if (altitude < 6000 && altitude > -1) {
                this.altitude = altitude;
            } else {
                throw new IllegalArgumentException("Altitude cannot be set higher than 5000 and less than 0");
            }
        }
        /**
         * Gets the current "reroll" token.
         *
         * @return the "reroll" token object.
         */
        public Token getRerollToken(){
            return rerollToken;
        }
}
