package Decoder.TEMP.BlockTypes.properties;

import Decoder.TEMP.BlockTypes.nPropertyID;

public class unknown implements nPropertyID {

    String name;

    public unknown(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " decoding hasn't been implemented yet";
    }

    @Override
    public String getProp(){
        return "unknown";
    }
}
