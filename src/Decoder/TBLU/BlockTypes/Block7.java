package Decoder.TBLU.BlockTypes;

public class Block7 implements Block{
    String hash1;
    String unkownValue1;
    String name1;
    String hash2;
    String unkownValue2;
    String name2;
    String name3;
    String name4;

    public Block7(String hash1, String unkownValue1, String name1, String hash2, String unkownValue2, String name2, String name3, String name4) {
        this.hash1 = hash1;
        this.unkownValue1 = unkownValue1;
        this.name1 = name1;
        this.hash2 = hash2;
        this.unkownValue2 = unkownValue2;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
    }

    public String getHash1() {
        return hash1;
    }

    public String getUnkownValue1() {
        return unkownValue1;
    }

    public String getName1() {
        return name1;
    }

    public String getHash2() {
        return hash2;
    }

    public String getUnkownValue2() {
        return unkownValue2;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }

    public String getName4() {
        return name4;
    }

    @Override
    public String toString() {
        return "Block7{" +
                "hash1='" + hash1 + '\'' +
                ", unkownValue1='" + unkownValue1 + '\'' +
                ", name1='" + name1 + '\'' +
                ", hash2='" + hash2 + '\'' +
                ", unkownValue2='" + unkownValue2 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3='" + name3 + '\'' +
                ", name4='" + name4 + '\'' +
                '}';
    }
}
