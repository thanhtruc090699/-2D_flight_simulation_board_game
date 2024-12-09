package com.mygdx.skyteam.logic;

public class Altitude{
        private int altitude;
        private Token rerollToken;
    
        public Altitude(){
            altitude=6000;
            rerollToken=new Token("reroll", 1);
        }
    
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

        public int getAltitudeValue(){
            return altitude;
        }
        
        public void setAltitude(int altitude){
            if (altitude < 6000 && altitude > -1) {
                this.altitude = altitude;
            } else {
                throw new IllegalArgumentException("Altitude cannot be set higher than 5000 and less than 0");
            }
        }

        public Token getRerollToken(){
            return rerollToken;
        }
}