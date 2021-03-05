package Decoder.TBLU.BlockTypes;

public class Block0_0 implements subBlock {

    private int blockType;
    private String string1;
    private String parentName;
    private String parentHash;
    private String string2;

    public Block0_0(String string1, String parentName, String parentHash, String string2) {
        this.blockType = blockType;
        this.string1 = string1;
        this.parentName = parentName;
        this.parentHash = parentHash;
        this.string2 = string2;
        this.blockType = 0;
    }

    public Block0_0(){
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
