package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class uint32 implements nProperty {

    private long value;

    public uint32(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
