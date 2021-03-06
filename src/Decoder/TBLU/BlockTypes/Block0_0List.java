package Decoder.TBLU.BlockTypes;

import java.util.ArrayList;

public class Block0_0List implements subBlock{

    private int blockType;
    private ArrayList<Block0_0> blocks;

    public Block0_0List(ArrayList<Block0_0> blocks) {
        this.blocks = blocks;
        this.blockType = 0;
    }

    public Block0_0List() {
        this.blockType = 0;
    }


    public ArrayList<Block0_0> getBlocks() {
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
