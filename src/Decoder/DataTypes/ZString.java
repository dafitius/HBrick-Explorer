package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class ZString implements nProperty {

    private String value;

    public ZString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
