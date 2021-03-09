package Decoder.TEMP.BlockTypes;

public class Header {

    private String fileType;
    private int fileSize;
    private String unknown;
    private String unkown2 = "";


    public Header() {
    }

    public Header(String fileType, int fileSize, String unknown, String unkown2) {
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.unknown = unknown;
        this.unkown2 = unkown2;
    }

    @Override
    public String toString() {
        return "Header{" +
                "fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", unknown='" + unknown + '\'' +
                ", unkown2='" + unkown2 + '\'' +
                '}';
    }

}
