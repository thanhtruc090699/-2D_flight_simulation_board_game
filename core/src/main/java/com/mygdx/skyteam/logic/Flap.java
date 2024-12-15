package com.mygdx.skyteam.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Flap{
    private ArrayList<Field> flapsFields;

    public Flap(){
         flapsFields = new ArrayList<>();
           flapsFields.add(new Field("Flap 1", Arrays.asList(1, 2), 1143, 761));
           flapsFields.add(new Field("Flap 2", Arrays.asList(2, 3), 1143, 761));
           flapsFields.add(new Field("Flap 3", Arrays.asList(4, 5), 1141, 868));
           flapsFields.add(new Field("Flap 4", Arrays.asList(5, 6), 1141, 972));
    
    }

    public void deployFlaps(int coPilotInput, Airplane airplane, int fieldChoice){

        
            Field selectedFlap = flapsFields.get(fieldChoice - 1);
            
            if (!selectedFlap.getValidDiceValues().contains(coPilotInput)) {
                System.out.println("Invalid dice value for " + selectedFlap.getName() + "!");
                return;  
            }
        
            
            if (fieldChoice > 1 && !flapsFields.get(fieldChoice - 2).isFilled()) {  //-2 because the input is not index based
                System.out.println("Flap " + (fieldChoice - 1) + " should be deployed first.");
                return;  
            }
        
           
            selectedFlap.setDiceValue(coPilotInput);
            airplane.getEngine().shiftOrangeMarker();
            System.out.println("Flap " + fieldChoice + " is now deployed.");
    }

    public ArrayList<Field> getFlapsFields(){
        return flapsFields;
    }
    
}