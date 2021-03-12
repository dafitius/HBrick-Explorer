package Decoder.TEMP.BlockTypes;

public class SEntityTemplateReference {

    private long entityID;
    private int entityIndex;
    private String exposedEntity;
    private int externalSceneIndex;

    public SEntityTemplateReference(long entityID, int entityIndex, String exposedEntity, int externalSceneIndex) {
        this.entityID = entityID;
        this.entityIndex = entityIndex;
        this.exposedEntity = exposedEntity;
        this.externalSceneIndex = externalSceneIndex;
    }

    @Override
    public String toString() {
        return "SEntityTemplateReference{" +
                "EntityID=" + Long.toUnsignedString(entityID) +
                ", entityIndex=" + entityIndex +
                ", exposedEntity='" + exposedEntity + '\'' +
                ", externalSceneIndex=" + externalSceneIndex +
                '}';
    }

    public long getEntityID() {
        return entityID;
    }

    public int getEntityIndex() {
        return entityIndex;
    }

    public String getExposedEntity() {
        return exposedEntity;
    }

    public int getExternalSceneIndex() {
        return externalSceneIndex;
    }
}
