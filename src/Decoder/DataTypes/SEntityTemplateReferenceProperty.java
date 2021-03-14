package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class SEntityTemplateReferenceProperty implements nProperty {
    Decoder.TEMP.BlockTypes.SEntityTemplateReference SEntityTemplateReference;


    public SEntityTemplateReferenceProperty(Decoder.TEMP.BlockTypes.SEntityTemplateReference SEntityTemplateReference) {
        this.SEntityTemplateReference = SEntityTemplateReference;
    }


    @Override
    public String toString() {
        return  SEntityTemplateReference + "";
    }


    public Decoder.TEMP.BlockTypes.SEntityTemplateReference getSEntityTemplateReference() {
        return SEntityTemplateReference;
    }
}
