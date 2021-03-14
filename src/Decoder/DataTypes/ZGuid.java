package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class ZGuid implements nProperty {

    private String repositoryId;

    public ZGuid(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    @Override
    public String toString() {
        return repositoryId;
    }

}
