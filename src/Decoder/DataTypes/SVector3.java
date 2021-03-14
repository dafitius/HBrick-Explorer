package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class SVector3 implements nProperty {

    private float x;
    private float y;
    private float z;

    public SVector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "\"x\": " + x + "," + "\"y\": " + y + "," + "\"z\": " + z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
