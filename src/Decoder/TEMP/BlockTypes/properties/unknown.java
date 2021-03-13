package Decoder.TEMP.BlockTypes.properties;

import Decoder.TEMP.BlockTypes.nPropertyID;

public class unknown implements nPropertyID {

    String name = "this property's";

    public unknown(String name) {
        if(name != null) {
            this.name = name;
        } else name = "this property";
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
