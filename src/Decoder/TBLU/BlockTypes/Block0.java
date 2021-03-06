package Decoder.TBLU.BlockTypes;

public class Block0 implements Block {

    private String parentName;
    private String parentHash;
    private String type;
    private String hash;
    private String name;
    private Block0_0List subBlock0List;
    private Block0_1 subBlock1;
    private Block0_2 subBlock2;
    private Block0_3 subBlock3;

    public Block0(String parentName, String parentHash, String type, String hash, String name, Block0_0List subBlock0List, Block0_1 subBlock1, Block0_2 subBlock2, Block0_3 subBlock3) {
        this.parentName = parentName;
        this.parentHash = parentHash;
        this.type = type;
        this.hash = hash;
        this.name = name;
        this.subBlock0List = subBlock0List;
        this.subBlock1 = subBlock1;
        this.subBlock2 = subBlock2;
        this.subBlock3 = subBlock3;
    }


    public String getParentName() {
        return parentName;
    }

    public String getParentHash() {
        return parentHash;
    }

    public String getType() {
        return type;
    }

    public String getHash() {
        return hash;
    }

    public String getName() {
        return name;
    }

    public Block0_0List getSubBlock0List() {
        return subBlock0List;
    }

    public Block0_1 getSubBlock1() {
        return subBlock1;
    }

    public Block0_2 getSubBlock2() {
        return subBlock2;
    }

    public Block0_3 getSubBlock3() {
        return subBlock3;
    }


    @Override
    public String toString() {
        return "Block0{" +
                "parentName='" + parentName + '\'' +
                ", parentHash='" + parentHash + '\'' +
                ", type='" + type + '\'' +
                ", hash='" + hash + '\'' +
                ", name='" + name + '\'' +
                ", subBlock0=" + subBlock0List +
                ", subBlock1=" + subBlock1 +
                ", subBlock2=" + subBlock2 +
                ", subBlock3=" + subBlock3 +
                '}';
    }
}
