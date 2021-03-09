package Decoder.TEMP;

import Decoder.BlockAdress;
import Decoder.TEMP.BlockTypes.*;
import Decoder.Tools;
import Files.TEMP;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TEMPDecoder {

    File TEMPfile;
    byte[] fileInBytes;
    private Map<Integer, String> keyToType;

    private Header header;
    private ArrayList<Block0> block0;
    private ArrayList<Block1> block1;
    private ArrayList<Block2> block2;
    private Footer footer;

    public TEMPDecoder() {

    }


    public TEMP decode(File file) throws IOException{
        this.header = new Header();
        this.block0 = new ArrayList<>();

        this.TEMPfile = file;
        this.fileInBytes = Files.readAllBytes(Paths.get(file.getPath()));
        System.out.println("start decode");
        if(Tools.isBIN1File(this.fileInBytes)){
            //header size is 0x18, so that will be skipped,
            this.header = readHeader(this.fileInBytes);
            this.keyToType = readFooter();
            System.out.println(this.header);
            ArrayList<BlockAdress> blockAdresses = Tools.scanForBlockAdress(this.fileInBytes, 0x20, 0x68);

            for(BlockAdress ba : blockAdresses){
               readData(ba.getPosition(), ba.getStartPos(), ba.getEndPos());
            }
        }
        return new TEMP();
    }

    public HashMap<Integer, String> readFooter(){
        HashMap<Integer, String> map = new HashMap<>();
        int atOffset = Integer.parseInt(Tools.readHexAsStringReverse(this.fileInBytes, 0x8, 0x4), 16);
        atOffset += 0x14;
        atOffset += Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
        atOffset += 0x4;
        if(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4).equals("3989BF9F") ){
            atOffset += 0x8;
            int skipAmount = (Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16) * 4);
            atOffset += skipAmount;
            atOffset += 0x4;
        }
        else return null;
        int numItems = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
        atOffset += 0x4;
        for (int i = 0; i < numItems; i++) {
            int stringLength = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x8, 0x4), 16) - 1;
            map.put(i, Tools.readString(this.fileInBytes, atOffset + 0xC, stringLength));
            atOffset += 0xC;
            atOffset += stringLength;
            if(stringLength % 4 == 0){
                atOffset += 0x4;
            }
            else {
                int padding = stringLength;
                while (padding % 4 != 0) {
                    atOffset++;
                    padding++;
                }
            }
        }
        System.out.println(map);
        return map;
    }

    public Header readHeader(byte[] bytes){
        String fileType;
        int fileSize;
        String unknown;
        String unkown2 = "";

        fileType = Tools.readHexAsString(bytes, 0x4, 0x4);

        fileSize = Integer.parseInt(Tools.readHexAsStringReverse(bytes, 0x8, 0x4), 16);
        fileSize += (Integer.parseInt(Tools.readHexAsString(bytes, fileSize + 0x18, 0x4), 16) * 0x4);

        fileSize += 0x1C;
        if(fileSize < bytes.length){
            fileSize += (Integer.parseInt(Tools.readHexAsString(bytes, fileSize + 0x4, 0x4), 16));
            fileSize += 0x8;
        }

        unknown = Tools.readHexAsString(bytes, 0x10, 0x4);

        String parentIndex = Tools.readHexAsString(bytes, 0x14, 0x4);
        unkown2 = Tools.readHexAsString(bytes, 0x18, 0x4);


        return new Header(fileType, fileSize, unknown, unkown2);

    }


    public void readData(int type, int from, int to){
            int atOffset = from;
            int numItems = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
            atOffset += 0x4;



        switch(type){
            case 0:
                this.block0 = readBlock0(numItems, atOffset);
                break;
            case 1:
                this.block1 = readBlock1(numItems, atOffset);
                break;
            case 2:
                this.block2 = readBlock2(numItems, atOffset);
                break;

        }

    }

    public ArrayList<Block0> readBlock0(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block0> blocks = new ArrayList<>();
        //data to pull
        String parentIndex = "";
        String string = "";
        String index = "";


        for (int i = 0; i < numItems; i++) {

            parentIndex = Tools.readHexAsString(this.fileInBytes, atOffset + 0xc, 0x4);


            string = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x18);
            index = Tools.readHexAsString(this.fileInBytes, atOffset + 0x20, 0x4);
            atOffset += (0x28);



            ArrayList<BlockAdress> blockAdresses = Tools.scanForBlockAdress(this.fileInBytes, atOffset, atOffset + 0x30);
            for (BlockAdress ba : blockAdresses) {
                Decoder.TEMP.BlockTypes.subBlock foundBlock = readBlock0SubData(ba.getPosition(), ba.getStartPos(), ba.getEndPos());
            }
            atOffset += (0x18 * 2);


            blocks.add(new Block0(parentIndex, string, index));


        }
        for(Block0 block : blocks){
            if(!block.getString().equals("")){
                //System.out.println(block);
            }
        }
        return blocks;
    }

    public ArrayList<Block1> readBlock1(int numItems, int startOffset){
        int atOffset = startOffset;

        ArrayList<Block1> blocks = new ArrayList<>();
        String parentName;
        String parentHash;
        for (int i = 0; i < numItems; i++) {
            atOffset += 0x4;
        }

        return blocks;
    }

    public ArrayList<Block2> readBlock2(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block2> blocks = new ArrayList<>();
        String parent1Name = "";
        String parent2Name = "";
        String parent1Hash = "";
        String parent2Hash = "";
        String string1 = "";
        String string2 = "";

        for (int i = 0; i < numItems; i++) {
            String parent1Index = Tools.readHexAsString(this.fileInBytes, atOffset, 0x4);
            String parent2Index = Tools.readHexAsString(this.fileInBytes, atOffset + 0x4, 0x4);
            string1 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x10);
            string2 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x20);

            atOffset += 0x38;
        }
        return blocks;
    }


    public subBlock readBlock0SubData(int type, int from, int to){
        int atOffset = from;

        int numItems = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
        atOffset += 0x4;



        switch(type){
            case 0:
                readBlock0_0(numItems, atOffset);
            case 1:
                readBlock0_1(numItems, atOffset);
        }

        return null;
    }

    public void readBlock0_0(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block0_0> blocks = new ArrayList<>();
        String hash;
        String type = "";
        String unknown = "";
        for (int i = 0; i < numItems; i++) {

            hash = Tools.readHexAsString(this.fileInBytes, atOffset, 0x4);

            unknown = Tools.readHexAsString(this.fileInBytes, atOffset + 0x10, 0x4);

            String typeIndex = Tools.readHexAsString(this.fileInBytes, atOffset + 0x8, 0x4);
            if(Tools.isParsable(typeIndex, 16)){
                if (keyToType.containsKey(Integer.parseInt(typeIndex, 16))){
                    type = keyToType.get(Integer.parseInt(typeIndex, 16));
                }
            } else {
                type = typeIndex;
            }



            blocks.add(new Block0_0(hash, type, unknown));
            atOffset += 0x18;
        }


        System.out.println(blocks);
        //return new Block0_0List(blocks);
    }

    public void readBlock0_1(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block0_1> blocks = new ArrayList<>();
        String hash;
        String type = "";
        String unknown = "";
        for (int i = 0; i < numItems; i++) {

            hash = Tools.readHexAsString(this.fileInBytes, atOffset, 0x4);

            unknown = Tools.readHexAsString(this.fileInBytes, atOffset + 0x10, 0x4);

            String typeIndex = Tools.readHexAsString(this.fileInBytes, atOffset + 0x8, 0x4);
            if(Tools.isParsable(typeIndex, 16)){
                if (keyToType.containsKey(Integer.parseInt(typeIndex, 16))){
                    type = keyToType.get(Integer.parseInt(typeIndex, 16));
                }
            } else {
                type = typeIndex;
            }



            blocks.add(new Block0_1(hash, type, unknown));
            atOffset += 0x18;
        }


        //System.out.println(blocks);
    }

    public void readBlock0_2(int numItems, int startOffset){
        int atOffset = startOffset;
        String name = "";
        String parentName = "";
        String parentHash = "";
        ArrayList<Map.Entry<String, Map.Entry<String, String>>> map = new ArrayList<>();


        for (int i = 0; i < numItems; i++) {
            name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x8);
            String parentIndex = Tools.readHexAsString(this.fileInBytes, atOffset + 0x10, 0x4);


            map.add(new AbstractMap.SimpleEntry(name, new AbstractMap.SimpleEntry(parentName, parentHash)));

            atOffset += 0x18;
        }


        //return new Block0_2(map);
    }

    public void readBlock0_3(int numItems, int startOffset){
        int atOffset = startOffset;
        String name = "";
        Map<String, ArrayList<String>> map = new HashMap<>();
        for (int i = 0; i < numItems; i++) {


            name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x8);


            ArrayList<String> strings = new ArrayList<>();
            ArrayList<BlockAdress> blockAdresses = Tools.scanForBlockAdress(this.fileInBytes, atOffset + 0x10, atOffset + 0x28);
            for (BlockAdress ba : blockAdresses) {
                int atSubOffset = ba.getStartPos();
                int numInstances = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                atSubOffset += 0x4;

                for (int j = 0; j < numInstances; j++) {
                    String instance = Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4);

                    atSubOffset += 0x4;
                }
            }
            map.put(name, strings);
            atOffset += (0x28);

        }
        //return new Block0_3(map);
    }

}
