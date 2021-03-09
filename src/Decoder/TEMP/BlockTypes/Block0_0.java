package Decoder.TEMP.BlockTypes;

public class Block0_0 implements subBlock{

    private String hash;
    private String number = "";
    private String unknown = "";

    public Block0_0(String hash, String number, String unknown) {
        this.hash = hash;
        this.number = number;
        this.unknown = unknown;
    }

    public Block0_0() {
    }

    public String getHash() {
        return hash;
    }

    public String getNumber() {
        return number;
    }

    public String getUnknown() {
        return unknown;
    }

    @Override
    public String toString() {
        return "Block0_0{" +
                "hash='" + hash + '\'' +
                ", number='" + number + '\'' +
                ", unknown='" + unknown + '\'' +
                '}';
    }

    @Override
    public int getType() {
        return 0;
    }
}
