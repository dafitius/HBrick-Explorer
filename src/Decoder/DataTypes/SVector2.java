package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class SVector2 implements nProperty {

    private float x;
    private float y;

    public SVector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "\"x\": " + x + "," + "\"y\": " + y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
