package Decoder.TBLU.BlockTypes;

import java.util.ArrayList;

public class propertyAliases implements subBlock{

    private int blockType;
    private ArrayList<propertyAlias> blocks;

    public propertyAliases(ArrayList<propertyAlias> blocks) {
        this.blocks = blocks;
        this.blockType = 0;
    }

    public propertyAliases() {
        this.blockType = 0;
    }


    public ArrayList<propertyAlias> getBlocks() {
        return blocks;
    }

    @Override
    public int getType() {
        return blockType;
    }

    @Override
    public String toString() {
        if (blocks == null) {
            return "NOFOUND";
        } else {
            return "Block0_0List{" +
                    "blockType=" + blockType +
                    ", blocks=" + blocks +
                    '}';
        }
    }
}
