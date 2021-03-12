package Decoder.TEMP.BlockTypes;

public class SEntityTemplateProperty {

    private nPropertyID nPropertyID;


    public SEntityTemplateProperty(Decoder.TEMP.BlockTypes.nPropertyID nPropertyID) {
        this.nPropertyID = nPropertyID;
    }

    @Override
    public String toString() {
        return "SEntityTemplateProperty{" +
                "nPropertyID=" + nPropertyID +
                '}';
    }

    public Decoder.TEMP.BlockTypes.nPropertyID getnPropertyID() {
        return nPropertyID;
    }
}
