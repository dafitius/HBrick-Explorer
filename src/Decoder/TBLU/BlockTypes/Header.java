package Decoder.TBLU.BlockTypes;

public class Header {

    private String fileType;
    private int fileSize;
    private String unknown;
    private String rootName;
    private String rootHash;


    public Header() {
    }

    public Header(String fileType, int fileSize, String unknown, String rootName, String rootHash) {
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.unknown = unknown;
        this.rootName = rootName;
        this.rootHash = rootHash;
    }

    public String getFileType() {
        return fileType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getUnknown() {
        return unknown;
    }

    public String getRootName() {
        return rootName;
    }

    public String getRootHash() {
        return rootHash;
    }

    @Override
    public String toString() {
        return "Header{" +
                "fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", unknown='" + unknown + '\'' +
                ", rootName='" + rootName + '\'' +
                ", rootHash='" + rootHash + '\'' +
                '}';
    }
}
