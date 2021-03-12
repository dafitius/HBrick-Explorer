package Decoder.TEMP.BlockTypes;

public class SEntityTemplatePropertyOverride {
    private SEntityTemplateReference propertOwner;
    private SEntityTemplateProperty propertyValue;

    public SEntityTemplatePropertyOverride(SEntityTemplateReference propertOwner, SEntityTemplateProperty propertyValue) {
        this.propertOwner = propertOwner;
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return "SEntityTemplatePropertyOverride{" +
                "propertOwner=" + propertOwner +
                ", propertyValue=" + propertyValue +
                '}';
    }

    public SEntityTemplateReference getPropertOwner() {
        return propertOwner;
    }

    public SEntityTemplateProperty getPropertyValue() {
        return propertyValue;
    }
}
