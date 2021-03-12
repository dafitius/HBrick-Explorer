package Decoder.TEMP.BlockTypes;

import java.util.ArrayList;

public class STemplateFactorySubEntity {
    private int entityTypeResourceIndex;
    private SEntityTemplateReference logicalParent;
    //private ?? platformSpecificPropertyValues;
    private ArrayList<SEntityTemplateProperty> postInitPropertyValues;
    private ArrayList<SEntityTemplateProperty> propertyValues;

    public STemplateFactorySubEntity(int entityTypeResourceIndex, SEntityTemplateReference logicalParent, ArrayList<SEntityTemplateProperty> postInitPropertyValues, ArrayList<SEntityTemplateProperty> propertyValues) {
        this.entityTypeResourceIndex = entityTypeResourceIndex;
        this.logicalParent = logicalParent;
        this.postInitPropertyValues = postInitPropertyValues;
        this.propertyValues = propertyValues;
    }

    @Override
    public String toString() {
        return "STemplateFactorySubEntity{" +
                "entityTypeResourceIndex=" + entityTypeResourceIndex +
                ", logicalParent=" + logicalParent +
                ", postInitPropertyValues=" + postInitPropertyValues +
                ", propertyValues=" + propertyValues +
                '}';
    }

    public int getEntityTypeResourceIndex() {
        return entityTypeResourceIndex;
    }

    public SEntityTemplateReference getLogicalParent() {
        return logicalParent;
    }

    public ArrayList<SEntityTemplateProperty> getPostInitPropertyValues() {
        return postInitPropertyValues;
    }

    public ArrayList<SEntityTemplateProperty> getPropertyValues() {
        return propertyValues;
    }
}
