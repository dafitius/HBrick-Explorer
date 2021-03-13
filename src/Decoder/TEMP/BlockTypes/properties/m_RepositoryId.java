package Decoder.TEMP.BlockTypes.properties;

import Decoder.TEMP.BlockTypes.nPropertyID;

public class m_RepositoryId implements nPropertyID {

    private String repositoryId;

    public m_RepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    @Override
    public String toString() {
        return "\"m_RepositoryId\": {\n" + repositoryId + "}";
    }

    @Override
    public String getProp() {
        return "m_RepositoryId";
    }
}
