package Decoder.TBLU.BlockTypes;

public class Block5 implements Block{
    String hash;
    String unkownValue; //seems to always be FFFFFFFE
    String name;

    public Block5(String hash, String unkownValue, String name) {
        this.hash = hash;
        this.unkownValue = unkownValue;
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public String getUnkownValue() {
        return unkownValue;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Block5{" +
                "hash='" + hash + '\'' +
                ", unkownValue='" + unkownValue + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
