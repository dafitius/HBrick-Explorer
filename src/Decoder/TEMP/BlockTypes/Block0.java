package Decoder.TEMP.BlockTypes;

public class Block0 implements Block{
    private String parentIndex = "";
    private String string = "";
    private String index = "";

    public Block0(String parentIndex, String string, String index) {
        this.parentIndex = parentIndex;
        this.string = string;
        this.index = index;
    }

    public Block0() {
    }

    public String getParentIndex() {
        return parentIndex;
    }

    public String getString() {
        return string;
    }

    public String getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Block0{" +
                "parentIndex='" + parentIndex + '\'' +
                ", string='" + string + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}
