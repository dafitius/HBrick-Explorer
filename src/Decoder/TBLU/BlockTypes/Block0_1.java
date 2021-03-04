package Decoder.TBLU.BlockTypes;

public class Block0_1 implements subBlock {


    private int blockType;

    public Block0_1(){
        blockType = 1;
    }

    @Override
    public int getType() {
        return blockType;
    }
}
