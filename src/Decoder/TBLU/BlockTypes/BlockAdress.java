package Decoder.BlockTypes;

import Decoder.Decoder;

public class BlockAdress {
    private int position;
    private int startPos;
    private int endPos;

    public BlockAdress(int position, int startPos, int endPos){
        this.position = position;
        this.startPos = startPos + 0xC;
        this.endPos = endPos + 0xC;
    }

    public int getPosition() {
        return position;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }
}
