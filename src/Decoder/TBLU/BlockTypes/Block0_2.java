package Decoder.TBLU.BlockTypes;

public class Block0_2 implements subBlock{

    private int blockType;

    public Block0_2(){
        blockType = 2;
    }

    @Override
    public int getType() {
        return blockType;
    }
}
