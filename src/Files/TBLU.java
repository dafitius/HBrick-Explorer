package Files;

import Decoder.TBLU.BlockTypes.*;

import java.util.ArrayList;

public class TBLU {

    private Header header;
    private ArrayList<subEntity> subEntities;
    private ArrayList<externalSceneTypeIndex> externalSceneTypeIndices;
    private ArrayList<Block2> Block2s;
    private ArrayList<Block3> block3;
    private ArrayList<Block4> block4;
    private ArrayList<overrideDelete> overrideDeletes;
    private ArrayList<pinConnectionOverrideDelete> pinConnectionOverrideDeletes;
    private Footer footer;

    public TBLU(Header header, ArrayList<subEntity> subEntities, ArrayList<externalSceneTypeIndex> externalSceneTypeIndices, ArrayList<Block2> Block2s, ArrayList<Block3> block3, ArrayList<Block4> block4, ArrayList<overrideDelete> overrideDeletes, ArrayList<pinConnectionOverrideDelete> pinConnectionOverrideDeletes, Footer footer) {
        this.header = header;
        this.subEntities = subEntities;
        this.externalSceneTypeIndices = externalSceneTypeIndices;
        this.Block2s = Block2s;
        this.block3 = block3;
        this.block4 = block4;
        this.overrideDeletes = overrideDeletes;
        this.pinConnectionOverrideDeletes = pinConnectionOverrideDeletes;
        this.footer = footer;
    }


    public Header getHeader() {
        return header;
    }

    public ArrayList<subEntity> getBlock0() {
        return subEntities;
    }

    public ArrayList<externalSceneTypeIndex> getBlock1() {
        return externalSceneTypeIndices;
    }

    public ArrayList<Block2> getBlock2() {
        return Block2s;
    }

    public ArrayList<Block3> getBlock3() {
        return block3;
    }

    public ArrayList<Block4> getBlock4() {
        return block4;
    }

    public ArrayList<overrideDelete> getBlock5() {
        return overrideDeletes;
    }

    public ArrayList<pinConnectionOverrideDelete> getBlock7() {
        return pinConnectionOverrideDeletes;
    }

    public Footer getFooter() {
        return footer;
    }


    public String printHeader(){
        return "[HEADER]" + "\n" +
                "size: " + getHeader().getFileSize() + " bytes" + "\n" +
                "type: " + getHeader().getFileType() + "\n" +
                "root: " + getHeader().getRootName() + "\n" +
                "unkown: " + getHeader().getUnknown() + "\n";
    }

    @Override
    public String toString() {
        return "TBLU{" +
                "header=" + header +
                ", block0=" + subEntities +
                ", block1=" + externalSceneTypeIndices +
                ", block2=" + Block2s +
                ", block3=" + block3 +
                ", block4=" + block4 +
                ", block5=" + overrideDeletes +
                ", block7=" + pinConnectionOverrideDeletes +
                ", footer=" + footer +
                '}';
    }
}
