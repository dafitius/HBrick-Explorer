package Decoder.TEMP.BlockTypes.properties;

import Decoder.TEMP.BlockTypes.SEntityTemplateReference;
import Decoder.TEMP.BlockTypes.nPropertyID;

public class m_eidParent implements nPropertyID {
    Decoder.TEMP.BlockTypes.SEntityTemplateReference SEntityTemplateReference;


    public m_eidParent(Decoder.TEMP.BlockTypes.SEntityTemplateReference SEntityTemplateReference) {
        this.SEntityTemplateReference = SEntityTemplateReference;
    }

    @Override
    public String toString() {
        return "m_eidParent{" +
                "SEntityTemplateReference=" + SEntityTemplateReference +
                '}';
    }

    @Override
    public String getProp(){
        return "m_eidParent";
    }

    public Decoder.TEMP.BlockTypes.SEntityTemplateReference getSEntityTemplateReference() {
        return SEntityTemplateReference;
    }
}
