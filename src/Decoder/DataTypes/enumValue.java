package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class enumValue implements nProperty {

    String value;

    public enumValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
