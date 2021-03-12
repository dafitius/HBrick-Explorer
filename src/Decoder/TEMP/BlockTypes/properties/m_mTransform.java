package Decoder.TEMP.BlockTypes.properties;

import Decoder.DataTypes.SMatrix34;
import Decoder.TEMP.BlockTypes.nPropertyID;

public class m_mTransform implements nPropertyID {
    private SMatrix34 matrix34;

    public m_mTransform(SMatrix34 matrix34) {
        this.matrix34 = matrix34;
    }

    @Override
    public String toString() {
        return "\"m_mTransform\": {\n" + matrix34 + "}";
    }

    @Override
    public String getProp(){
        return "m_mTransform";
    }

    public SMatrix34 getMatrix34() {
        return matrix34;
    }
}
