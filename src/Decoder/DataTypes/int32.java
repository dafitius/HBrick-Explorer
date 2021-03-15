package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class int32 implements nProperty {

    private long value;

    public int32(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value +"";
    }
}
