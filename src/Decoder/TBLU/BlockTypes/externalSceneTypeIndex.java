package Decoder.TBLU.BlockTypes;

public class externalSceneTypeIndex implements Block{

    String value;


    public externalSceneTypeIndex(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Block1{" +
                "value='" + value + '\'' +
                '}';
    }
}
