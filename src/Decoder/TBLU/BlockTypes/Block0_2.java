package Decoder.TBLU.BlockTypes;

import java.util.ArrayList;
import java.util.Map;

public class Block0_2 implements subBlock{

    private int blockType;
    private ArrayList<Map.Entry<String, Map.Entry<String, String>>> items;


    public Block0_2(ArrayList<Map.Entry<String, Map.Entry<String, String>>> items) {
        this.items = items;
        this.blockType = 2;
    }


    public Block0_2(){
        blockType = 2;
    }

    @Override
    public int getType() {
        return blockType;
    }

    public String toString() {
        if(items == null){
            return "NOFOUND";
        }else {
            return "Block0_1{" +
                    "arrays=" + items.toString() +
                    '}';
        }
    }
}
