package Decoder.TBLU.BlockTypes;

public class Block1 implements Block{

    String value;


    public Block1(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Block1{" +
                "value='" + value + '\'' +
                '}';
    }
}
