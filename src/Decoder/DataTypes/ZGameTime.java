package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class ZGameTime implements nProperty {

    private long value;

    public ZGameTime(long value) {
        this.value = value;
        System.out.println(value);
    }

    @Override
    public String toString() {
        return value + " ticks";
    }
}
