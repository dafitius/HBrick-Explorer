package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nPropertyID;

public class SMatrix34 {
    private SVector3 Trans;
    private SVector3 XAxis;
    private SVector3 YAxis;
    private SVector3 ZAxis;

    public SMatrix34(SVector3 trans, SVector3 XAxis, SVector3 YAxis, SVector3 ZAxis) {
        Trans = trans;
        this.XAxis = XAxis;
        this.YAxis = YAxis;
        this.ZAxis = ZAxis;
    }

    @Override
    public String toString() {
        return "\"Trans\": {" + Trans + "},\n"+
                "\"XAxis\": {" + XAxis + "},\n"+
                "\"YAxis\": {" + YAxis + "},\n"+
                "\"ZAxis\": {" + ZAxis + "},\n";
    }

    public SVector3 getTrans() {
        return Trans;
    }

    public SVector3 getXAxis() {
        return XAxis;
    }

    public SVector3 getYAxis() {
        return YAxis;
    }

    public SVector3 getZAxis() {
        return ZAxis;
    }
}
