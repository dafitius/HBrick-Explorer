package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class bool implements nProperty {

    boolean value;

    public bool(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ""+value;
    }

    public bool() {
    }
}
