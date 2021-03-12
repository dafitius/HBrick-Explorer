package Decoder.TEMP.BlockTypes.properties;

import Decoder.TEMP.BlockTypes.nPropertyID;

public class unknown implements nPropertyID {

    public unknown() {
    }

    @Override
    public String toString() {
        return "This data type has not been decoded yet";
    }

    @Override
    public String getProp(){
        return "unknown";
    }
}
