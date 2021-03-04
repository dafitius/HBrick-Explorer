package Files;

import Decoder.TBLU.BlockTypes.*;

public class TBLU {

    private Header header;
    private Block0 block0;
    private Block1 block1;
    private Block2 block2;
    private Block3 block3;
    private Block4 block4;
    private Block5 block5;
    private Block7 block7;
    private Footer footer;

    public TBLU(Header header, Block0 block0, Block1 block1, Block2 block2, Block3 block3, Block4 block4, Block5 block5, Block7 block7, Footer footer) {
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
}
