package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class float32 implements nProperty {

    private float value;

    public float32(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return ""+value;
    }
}
