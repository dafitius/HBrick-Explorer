package Decoder.TEMP.BlockTypes;

public class SEntityTemplateProperty {

    private String nPropertyID;
    private String type;
    private nProperty nProperty;


    public SEntityTemplateProperty(String nPropertyID, String type, Decoder.TEMP.BlockTypes.nProperty nProperty) {
        this.nPropertyID = nPropertyID;
        this.type = type;
        this.nProperty = nProperty;
    }

    @Override
    public String toString() {
        return  nPropertyID + "";
    }

    public Decoder.TEMP.BlockTypes.nProperty getnProperty() {
        return nProperty;
    }

    public String getnPropertyID() {
        return nPropertyID;
    }

    public String getType() {
        return type;
    }
}
