package Decoder.TBLU.BlockTypes;

public class propertyAlias implements subBlock {

    private int blockType;
    private String string1;
    private String parentName;
    private String parentHash;
    private String string2;

    public propertyAlias(String string1, String parentName, String parentHash, String string2) {
        this.blockType = blockType;
        this.string1 = string1;
        this.parentName = parentName;
        this.parentHash = parentHash;
        this.string2 = string2;
        this.blockType = 0;
    }

    public propertyAlias(){
        blockType = 0;
    }

    @Override
    public int getType() {
        return blockType;
    }




    @Override
    public String toString() {
        return "Block0_0{" +
                "blockType=" + blockType +
                ", string1='" + string1 + '\'' +
                ", parentName='" + parentName + '\'' +
                ", parentHash='" + parentHash + '\'' +
                ", string2='" + string2 + '\'' +
                '}';
    }

    public String printBlock(){
        return string1 + ", " + parentName + ", " + string2;
    }
}
