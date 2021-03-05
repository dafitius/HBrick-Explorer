package Files;

import Decoder.TBLU.BlockTypes.*;

import java.util.ArrayList;

public class TBLU {

    private Header header;
    private ArrayList<Block0> block0;
    private ArrayList<Block1> block1;
    private ArrayList<Block2> block2;
    private ArrayList<Block3> block3;
    private ArrayList<Block4> block4;
    private ArrayList<Block5> block5;
    private ArrayList<Block7> block7;
    private Footer footer;

    public TBLU(Header header, ArrayList<Block0> block0, ArrayList<Block1> block1, ArrayList<Block2> block2, ArrayList<Block3> block3, ArrayList<Block4> block4, ArrayList<Block5> block5, ArrayList<Block7> block7, Footer footer) {
        this.header = header;
        this.block0 = block0;
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
        this.block4 = block4;
        this.block5 = block5;
        this.block7 = block7;
        this.footer = footer;
    }


    public Header getHeader() {
        return header;
    }

    public ArrayList<Block0> getBlock0() {
        return block0;
    }

    public ArrayList<Block1> getBlock1() {
        return block1;
    }

    public ArrayList<Block2> getBlock2() {
        return block2;
    }

    public ArrayList<Block3> getBlock3() {
        return block3;
    }

    public ArrayList<Block4> getBlock4() {
        return block4;
    }

    public ArrayList<Block5> getBlock5() {
        return block5;
    }

    public ArrayList<Block7> getBlock7() {
        return block7;
    }

    public Footer getFooter() {
        return footer;
    }

    @Override
    public String toString() {
        return "TBLU{" +
                "header=" + header +
                ", block0=" + block0 +
                ", block1=" + block1 +
                ", block2=" + block2 +
                ", block3=" + block3 +
                ", block4=" + block4 +
                ", block5=" + block5 +
                ", block7=" + block7 +
                ", footer=" + footer +
                '}';
    }
}
