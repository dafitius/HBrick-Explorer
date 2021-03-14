package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class SColorRGB implements nProperty {

    private float r;
    private float g;
    private float b;

    public SColorRGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public String toString() {
        return "\"r\": " + r + "," + "\"g\": " + g + "," + "\"b\": " + b;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

}
