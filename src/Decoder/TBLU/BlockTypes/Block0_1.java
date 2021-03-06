package Decoder.TBLU.BlockTypes;

import java.util.ArrayList;
import java.util.Map;

public class Block0_1 implements subBlock {


    private int blockType;
    private Map<String, ArrayList<Map.Entry<Map.Entry<String, String>, String>>> arrays;

    public Block0_1(Map<String, ArrayList<Map.Entry<Map.Entry<String, String>, String>>> arrays) {
        this.blockType = 1;
        this.arrays = arrays;
    }

    public Map<String, ArrayList<Map.Entry<Map.Entry<String, String>, String>>> getArrays() {
        return arrays;
    }

    public Block0_1(){
        blockType = 1;
    }

    @Override
    public int getType() {
        return blockType;
    }

    @Override
    public String toString() {
        if(arrays == null){
            return "NOFOUND";
        }else {
            return "Block0_1{" +
                    "arrays=" + arrays.toString() +
                    '}';
        }
    }
}
