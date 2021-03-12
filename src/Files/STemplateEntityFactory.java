package Files;

import Decoder.TEMP.BlockTypes.SEntityTemplatePropertyOverride;
import Decoder.TEMP.BlockTypes.STemplateFactorySubEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class STemplateEntityFactory {

    private int blueprintIndexInResourceHeader;
    private int[] externalSceneTypeIndicesInResourceHeader;
    private ArrayList<SEntityTemplatePropertyOverride> propertyOverrides;
    private int rootEntity;
    private ArrayList<STemplateFactorySubEntity> subEntities;
    private int subType;

    public STemplateEntityFactory(int blueprintIndexInResourceHeader, int[] externalSceneTypeIndicesInResourceHeader, ArrayList<SEntityTemplatePropertyOverride> propertyOverrides, int rootEntity, ArrayList<STemplateFactorySubEntity> subEntities, int subType) {
        this.blueprintIndexInResourceHeader = blueprintIndexInResourceHeader;
        this.externalSceneTypeIndicesInResourceHeader = externalSceneTypeIndicesInResourceHeader;
        this.propertyOverrides = propertyOverrides;
        this.rootEntity = rootEntity;
        this.subEntities = subEntities;
        this.subType = subType;
    }

    @Override
    public String toString() {
        return "STemplateEntityFactory{" +
                "blueprintIndexInResourceHeader=" + blueprintIndexInResourceHeader + "\n" +
                ", externalSceneTypeIndicesInResourceHeader=" + Arrays.toString(externalSceneTypeIndicesInResourceHeader) + "\n" +
                ", propertyOverrides=" + propertyOverrides + "\n" +
                ", rootEntity=" + rootEntity + "\n" +
                ", subEntities=" + subEntities + "\n" +
                ", subType=" + subType + "\n" +
                '}';
    }

    public int getBlueprintIndexInResourceHeader() {
        return blueprintIndexInResourceHeader;
    }

    public int[] getExternalSceneTypeIndicesInResourceHeader() {
        return externalSceneTypeIndicesInResourceHeader;
    }

    public ArrayList<SEntityTemplatePropertyOverride> getPropertyOverrides() {
        return propertyOverrides;
    }

    public int getRootEntity() {
        return rootEntity;
    }

    public ArrayList<STemplateFactorySubEntity> getSubEntities() {
        return subEntities;
    }

    public int getSubType() {
        return subType;
    }
}
