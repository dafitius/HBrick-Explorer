package Decoder.TBLU.BlockTypes;

import java.util.ArrayList;
import java.util.Map;

public class entitySubsets implements subBlock{

    private int blockType;
    private Map<String, ArrayList<String>> arrays;

    public entitySubsets(){
        this.blockType = 3;
    }
    public entitySubsets(Map<String, ArrayList<String>> arrays) {
        this.blockType = 3;
        this.arrays = arrays;
    }

    public Map<String, ArrayList<String>> getArrays() {
        return arrays;
    }

    @Override
    public String toString() {
        if(arrays == null){
            return "NOFOUND";
        }else {
            return "Block0_3{" +
                    "arrays=" + arrays.toString() +
                    '}';
        }
    }

    @Override
    public int getType() {
        return blockType;
    }
}
