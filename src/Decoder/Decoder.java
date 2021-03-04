package Decoder;

import sample.Item;
import sample.ItemLibrary;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Decoder {

    File TBLUfile;
    byte[] fileInBytes;
    HashMap<Integer, String> keyAndName = new HashMap<>();
    HashMap<Integer, String> keyAndHash = new HashMap<>();
    ItemLibrary itemLibrary;

    public Decoder(ItemLibrary itemLibrary) {
        this.itemLibrary = itemLibrary;
    }



    public ItemLibrary decode(File file) throws IOException{
        this.TBLUfile = file;
        this.fileInBytes = Files.readAllBytes(Paths.get(file.getPath()));
        System.out.println("start decode");
        if(isBIN1File(this.fileInBytes)){
            //header size is 0x18, so that will be skipped,
            ArrayList<BlockAdress> blockAdresses = scanForBlockAdress(this.fileInBytes, 0x18, 0xD8);

            for(BlockAdress ba : blockAdresses){
                readData(ba.getPosition(), ba.getStartPos(), ba.getEndPos());
            }
        }
        return itemLibrary;
    }

    public boolean isBIN1File(byte[] fileInBytes){
        byte[] fileData = Arrays.copyOfRange(fileInBytes, 0, 4);
        String str = new String(fileData, 0, 4, StandardCharsets.UTF_8);

        if(str.equals("BIN1")){
            System.out.println("valid BIN1 file detected");
            return true;
        }
        else {
            System.out.println("not a valid BIN1 file");
            return false;
        }
    }

    public boolean isBlockAdress(String blockAdress, String row1, String row2, String row3){


        if(!row1.toLowerCase().contains("ffffffff")){
            if(row2.equals(row3)){
              return true;
            }
        }
        return false;
    }

    public String readHexString(byte[] bytes, int from, int amount){
        bytes = Arrays.copyOfRange(bytes, from, from + amount);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String byteString = String.format("%02X", b);
            sb.append(new StringBuilder(byteString).reverse().toString());
        }
        sb.reverse();
        return sb.toString();
    }

    public byte[] readBytes(byte[] bytes, int from, int amount){
        return Arrays.copyOfRange(bytes, from, from + amount);
    }

    public String readString(byte[] bytes, int from, int amount){
        byte[] strInBytes = Arrays.copyOfRange(bytes, from, from + amount);
        String str = new String(strInBytes, 0, strInBytes.length, StandardCharsets.UTF_8);
        return str;
    }

    public String readStringFromOffset(byte[] bytes, int stringOffsetOffset){
        int atOffset = stringOffsetOffset;

        int strOffset = Integer.parseInt(readHexString(bytes, atOffset, 0x4), 16) + 0x10;
        int strLength = Integer.parseInt(readHexString(bytes, strOffset - 0x4, 0x4), 16);
        return readString(this.fileInBytes, strOffset, strLength);
    }

    public ArrayList<BlockAdress> scanForBlockAdress(byte[] fileInBytes, int from, int to){
        int offset = from;
        int scanArea = to - from;

        ArrayList<BlockAdress> blockAdresses = new ArrayList<>();

        for (int i = 0; i < scanArea / 0x18; i++) {
            byte[] adressBlock = Arrays.copyOfRange(fileInBytes, offset, offset + 0x18);
            String adressBlockAsString = readHexString(adressBlock, 0x0, adressBlock.length);

            int rowLength = adressBlockAsString.length() / 3;
            String row3 = adressBlockAsString.substring(rowLength * 0, rowLength * 1);
            String row2 = adressBlockAsString.substring(rowLength * 1, rowLength * 2);
            String row1 = adressBlockAsString.substring(rowLength * 2, rowLength * 3);
            if(isBlockAdress(adressBlockAsString, row1, row2, row3)){
                blockAdresses.add(new BlockAdress(i,Integer.parseInt(row1.substring(row1.length()/2), 16), Integer.parseInt(row2.substring(row2.length()/2), 16)));
            }

            offset += 0x18;
        }
        return blockAdresses;
    }

    public void fillParentMaps(int numItems, int atOffset){
        for (int i = 0; i < numItems; i++) {
            String hash = readHexString(this.fileInBytes, atOffset + 0x28, 0x8);
            String name = readStringFromOffset(this.fileInBytes, atOffset + 0x40);
            keyAndHash.put(i, hash);
            keyAndName.put(i, name);
            atOffset += 0xA8;
        }
    }


    public void readData(int type, int from, int to){
            int atOffset = from;
            int numItems = Integer.parseInt(readHexString(this.fileInBytes, atOffset, 0x4), 16);
            atOffset += 0x4;
        System.out.println("  Type " + type + " found");
        System.out.println("  amount: " + numItems);

        switch(type){
            case 0:
                readBlock0(numItems, atOffset);
                break;
            case 1:
                readBlock1(numItems, atOffset);
                break;
            case 2: case 3: case 4:
                readBlock234(numItems, atOffset);
                break;
            case 5:
                readBlock5(numItems, atOffset);
                break;
            case 6:
                System.out.println("wait THATS ILLEGAL!");
                break;
            case 7:
                readBlock7(numItems, atOffset);
                break;

        }

    }

    public void readBlock0(int numItems, int startOffset){
        int atOffset = startOffset;

        //data to pull
        String name = "";
        String hash = "";
        String entityType = "";
        String parentHash = "";
        Map<String, ArrayList<String>> linkedData = new HashMap<>();

        fillParentMaps(numItems, atOffset);
        for (int i = 0; i < numItems; i++) {

            String parentIndex = readHexString(this.fileInBytes, atOffset + 0xc, 0x4);

            if(Tools.isParsable(parentIndex, 16)){
                if (keyAndHash.containsKey(Integer.parseInt(parentIndex, 16))){
                    parentHash = keyAndHash.get(Integer.parseInt(parentIndex, 16));
                }
            } else parentHash = parentIndex;
            entityType = readHexString(this.fileInBytes, atOffset + 0x20, 0x4);
            name = readStringFromOffset(this.fileInBytes, atOffset + 0x40);
            hash = readHexString(this.fileInBytes, atOffset + 0x28, 0x8);
            atOffset += (0x48);
            ArrayList<BlockAdress> blockAdresses = scanForBlockAdress(this.fileInBytes, atOffset, atOffset + 0x60);
            for (BlockAdress ba : blockAdresses) {
                linkedData = readBlock0SubData(ba.getPosition(), ba.getStartPos(), ba.getEndPos());
                try {
                    for (String key : linkedData.keySet()) {
                        System.out.println(key + ": "+ linkedData.get(key).toString());
                    }
                }catch (NullPointerException e){
                    System.out.println(e.getMessage());
                }
            }
            atOffset += (0x18 * 4);

            if(linkedData != null) {
                this.itemLibrary.add(new Item(parentHash, entityType, hash, name, linkedData));
            } else this.itemLibrary.add(new Item(parentHash, entityType, hash, name));

        }
    }
    public void readBlock1(int numItems, int startOffset){
        int atOffset = startOffset;
        System.out.println("type 1");
    }
    public void readBlock234(int numItems, int startOffset){
        int atOffset = startOffset;
        System.out.println("type: 2, 3, 4");
    }
    public void readBlock5(int numItems, int startOffset){
        int atOffset = startOffset;
        System.out.println("type 5");
    }
    public void readBlock7(int numItems, int startOffset){
        int atOffset = startOffset;
        System.out.println("type 7");
    }


    public Map<String, ArrayList<String>> readBlock0SubData(int type, int from, int to){
        int atOffset = from;
        int numItems = Integer.parseInt(readHexString(this.fileInBytes, atOffset, 0x4), 16);
        atOffset += 0x4;
        System.out.println("  sub-Type " + type + " found");
        System.out.println("  amount: " + numItems);

        Map<String, ArrayList<String>> map = new HashMap<>();

        switch(type){
            case 3:

                String name = "";
                for (int i = 0; i < numItems; i++) {


                    name = readStringFromOffset(this.fileInBytes, atOffset + 0x8);


                    ArrayList<String> strings = new ArrayList<>();
                    ArrayList<BlockAdress> blockAdresses = scanForBlockAdress(this.fileInBytes, atOffset + 0x10, atOffset + 0x28);
                    for (BlockAdress ba : blockAdresses) {
                        int atSubOffset = ba.getStartPos();
                        int numInstances = Integer.parseInt(readHexString(this.fileInBytes, atSubOffset, 0x4), 16);
                        atSubOffset += 0x4;
                        System.out.println(name);
                        System.out.println(Integer.toHexString(numInstances));
                        for (int j = 0; j < numInstances; j++) {
                            String instance = readHexString(this.fileInBytes, atSubOffset, 0x4);
                            if(Tools.isParsable(instance, 16)) {
                                if(keyAndHash.containsKey(Integer.parseInt(instance, 16))) {
                                    strings.add(keyAndName.get(Integer.parseInt(instance, 16)));
                                }
                            }
                            atSubOffset += 0x4;
                        }
                    }
                    map.put(name, strings);
                    atOffset += (0x28);

                }
                return map;
        }

        return null;
    }


}
