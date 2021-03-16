package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

import java.util.ArrayList;

public class TArray implements nProperty{

    ArrayList<nProperty> properties = new ArrayList<>();

    public TArray(ArrayList<nProperty> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        String returnString = "";
        for(nProperty property : properties){
            returnString += property.toString();
            returnString += "\n\n";

        }
        return returnString;
    }
}
