package Decoder.TBLU;

import Decoder.TBLU.BlockTypes.*;
import sample.ItemLibrary;
import Decoder.Tools;
import Files.TBLU;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.*;

public class TBLUDecoder {

    File TBLUfile;
    byte[] fileInBytes;
    HashMap<Integer, String> keyAndName = new HashMap<>();
    HashMap<Integer, String> keyAndHash = new HashMap<>();

    private Header header;
    private ArrayList<Block0> block0;
    private ArrayList<Block1> block1;
    private ArrayList<Block2> block2;
    private ArrayList<Block3> block3;
    private ArrayList<Block4> block4;
    private ArrayList<Block5> block5;
    private ArrayList<Block7> block7;
    private Footer footer;

    public TBLUDecoder() {

    }


    public TBLU decode(File file) throws IOException{
        this.header = new Header();
        this.block0 = new ArrayList<>();
        this.block1 = new ArrayList<>();
        this.block2 = new ArrayList<>();
        this.block3 = new ArrayList<>();
        this.block4 = new ArrayList<>();
        this.block5 = new ArrayList<>();
        this.block7 = new ArrayList<>();

        this.TBLUfile = file;
        this.fileInBytes = Files.readAllBytes(Paths.get(file.getPath()));
        System.out.println("start decode");
        if(Tools.isBIN1File(this.fileInBytes)){
            //header size is 0x18, so that will be skipped,
            fillParentMaps();
            this.header = readHeader(this.fileInBytes);
            ArrayList<BlockAdress> blockAdresses = Tools.scanForBlockAdress(this.fileInBytes, 0x18, 0xD8);

            for(BlockAdress ba : blockAdresses){
               readData(ba.getPosition(), ba.getStartPos(), ba.getEndPos());
            }
        }
        return new TBLU(this.header, this.block0, this.block1, this.block2, this.block3, this.block4, this.block5, this.block7, this.footer);
    }


    public void fillParentMaps(){

        int atOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, 0x18, 0x4), 16);
        int numItems = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0xc, 0x4), 16);
        atOffset += 0x10;

        for (int i = 0; i < numItems; i++) {
            String hash = Tools.readHexAsString(this.fileInBytes, atOffset + 0x28, 0x8);
            String name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x40);
            keyAndHash.put(i, hash);
            keyAndName.put(i, name);
            atOffset += 0xA8;
        }
    }


    public Header readHeader(byte[] bytes){
        String fileType;
        int fileSize;
        String unknown;
        String rootName = "";
        String rootHash = "";

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
        if(Tools.isParsable(parentIndex, 16)){
            if (keyAndHash.containsKey(Integer.parseInt(parentIndex, 16))){
                rootHash = keyAndHash.get(Integer.parseInt(parentIndex, 16));
                rootName = keyAndName.get(Integer.parseInt(parentIndex, 16));
            }
        } else {
            rootHash = parentIndex;
            rootName = parentIndex;
        }


        return new Header(fileType, fileSize, unknown, rootName, rootHash);

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
            case 3:
                this.block3 = readBlock3(numItems, atOffset);
                break;
            case 4:
                this.block4 = readBlock4(numItems, atOffset);
                break;
            case 5:
                this.block5 = readBlock5(numItems, atOffset);
                break;
            case 6:
                System.out.println("BLOCK6 DETECTED  ---------------------------------------------------------------------");
                break;
            case 7:
                this.block7 = readBlock7(numItems, atOffset);
                break;

        }

    }

    public ArrayList<Block0> readBlock0(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block0> blocks = new ArrayList<>();
        //data to pull
        String name = "";
        String hash = "";
        String entityType = "";
        String parentHash = "";
        String parentName = "";
        Block0_0List block0_0List;
        Block0_1 block0_1;
        Block0_2 block0_2;
        Block0_3 block0_3;


        for (int i = 0; i < numItems; i++) {

            block0_0List = new Block0_0List();
            block0_1 = new Block0_1();
            block0_2 = new Block0_2();
            block0_3 = new Block0_3();
            String parentIndex = Tools.readHexAsString(this.fileInBytes, atOffset + 0xc, 0x4);

            if(Tools.isParsable(parentIndex, 16)){
                if (keyAndHash.containsKey(Integer.parseInt(parentIndex, 16))){
                    parentHash = keyAndHash.get(Integer.parseInt(parentIndex, 16));
                    parentName = keyAndName.get(Integer.parseInt(parentIndex, 16));
                }
            } else {
                parentHash = parentIndex;
                parentName = parentIndex;
            }
            entityType = Tools.readHexAsString(this.fileInBytes, atOffset + 0x20, 0x4);
            name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x40);
            hash = Tools.readHexAsString(this.fileInBytes, atOffset + 0x28, 0x8);
            atOffset += (0x48);
            ArrayList<BlockAdress> blockAdresses = Tools.scanForBlockAdress(this.fileInBytes, atOffset, atOffset + 0x60);
            for (BlockAdress ba : blockAdresses) {
                subBlock foundBlock = readBlock0SubData(ba.getPosition(), ba.getStartPos(), ba.getEndPos());
                int BlockType = foundBlock.getType();

                if (BlockType == 0) block0_0List = (Block0_0List) foundBlock;
                if (BlockType == 1) block0_1 = (Block0_1) foundBlock;
                if (BlockType == 2) block0_2 = (Block0_2) foundBlock;
                if (BlockType == 3) block0_3 = (Block0_3) foundBlock;


            }
            atOffset += (0x18 * 4);


            blocks.add(new Block0(parentName, parentHash, entityType, hash, name, block0_0List, block0_1, block0_2, block0_3));


        }
        return blocks;
    }

    public ArrayList<Block1> readBlock1(int numItems, int startOffset){
        int atOffset = startOffset;

        ArrayList<Block1> blocks = new ArrayList<>();
        String parentName;
        String parentHash;
        for (int i = 0; i < numItems; i++) {
            blocks.add(new Block1(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4)));
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
            if(Tools.isParsable(parent1Index, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parent1Index, 16))){
                    parent1Name = keyAndName.get(Integer.parseInt(parent1Index, 16));
                    parent1Hash = keyAndHash.get(Integer.parseInt(parent1Index, 16));
                }
            } else {
                parent1Name = parent1Index;
                parent1Hash = parent1Index;
            }
            if(Tools.isParsable(parent2Index, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parent2Index, 16))){
                    parent2Name = keyAndName.get(Integer.parseInt(parent2Index, 16));
                    parent2Hash = keyAndHash.get(Integer.parseInt(parent2Index, 16));
                }
            } else {
                parent2Name = parent2Index;
                parent2Hash = parent2Index;
            }
            string1 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x10);
            string2 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x20);
            blocks.add(new Block2(parent1Name, parent2Name, parent1Hash, parent2Hash, string1, string2));

            atOffset += 0x38;
        }
        return blocks;
    }

    public ArrayList<Block3> readBlock3(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block3> blocks = new ArrayList<>();
        String parent1Name = "";
        String parent2Name = "";
        String parent1Hash = "";
        String parent2Hash = "";
        String string1 = "";
        String string2 = "";

        for (int i = 0; i < numItems; i++) {
            String parent1Index = Tools.readHexAsString(this.fileInBytes, atOffset, 0x4);
            String parent2Index = Tools.readHexAsString(this.fileInBytes, atOffset + 0x4, 0x4);
            if(Tools.isParsable(parent1Index, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parent1Index, 16))){
                    parent1Name = keyAndName.get(Integer.parseInt(parent1Index, 16));
                    parent1Hash = keyAndHash.get(Integer.parseInt(parent1Index, 16));
                }
            } else {
                parent1Name = parent1Index;
                parent1Hash = parent1Index;
            }
            if(Tools.isParsable(parent2Index, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parent2Index, 16))){
                    parent2Name = keyAndName.get(Integer.parseInt(parent2Index, 16));
                    parent2Hash = keyAndHash.get(Integer.parseInt(parent2Index, 16));
                }
            } else {
                parent2Name = parent2Index;
                parent2Hash = parent2Index;
            }
            string1 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x10);
            string2 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x20);
            blocks.add(new Block3(parent1Name, parent2Name, parent1Hash, parent2Hash, string1, string2));

            atOffset += 0x38;
        }
        return blocks;
    }

    public ArrayList<Block4> readBlock4(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block4> blocks = new ArrayList<>();
        String parent1Name = "";
        String parent2Name = "";
        String parent1Hash = "";
        String parent2Hash = "";
        String string1 = "";
        String string2 = "";

        for (int i = 0; i < numItems; i++) {
            String parent1Index = Tools.readHexAsString(this.fileInBytes, atOffset, 0x4);
            String parent2Index = Tools.readHexAsString(this.fileInBytes, atOffset + 0x4, 0x4);
            if(Tools.isParsable(parent1Index, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parent1Index, 16))){
                    parent1Name = keyAndName.get(Integer.parseInt(parent1Index, 16));
                    parent1Hash = keyAndHash.get(Integer.parseInt(parent1Index, 16));
                }
            } else {
                parent1Name = parent1Index;
                parent1Hash = parent1Index;
            }
            if(Tools.isParsable(parent2Index, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parent2Index, 16))){
                    parent2Name = keyAndName.get(Integer.parseInt(parent2Index, 16));
                    parent2Hash = keyAndHash.get(Integer.parseInt(parent2Index, 16));
                }
            } else {
                parent2Name = parent2Index;
                parent2Hash = parent2Index;
            }
            string1 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x10);
            string2 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x20);
            blocks.add(new Block4(parent1Name, parent2Name, parent1Hash, parent2Hash, string1, string2));

            atOffset += 0x38;
        }
        return blocks;
    }

    public ArrayList<Block5> readBlock5(int numItems, int startOffset){

        int atOffset = startOffset;
        ArrayList<Block5> blocks = new ArrayList<>();
        String hash;
        String unkownValue;
        String name;
        for (int i = 0; i < numItems; i++) {

            hash = Tools.readHexAsString(this.fileInBytes, atOffset, 0x8);
            unkownValue = Tools.readHexAsString(this.fileInBytes, atOffset + 0xC, 0x4);
            name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x18);

            blocks.add(new Block5(hash, unkownValue, name));
            atOffset += 0x20;
        }
        for(Block5 block : blocks){
            if(!block.getUnkownValue().equals("FFFFFFFE") || !block.getName().equals("")){
                System.out.println("THERE WAS A BLOCK5 WITH A SPECIAL VALUE ---------------------------------------------------------------------");
            }
        }
        return blocks;


    }

    public ArrayList<Block7> readBlock7(int numItems, int startOffset){
        int atOffset = startOffset;


        ArrayList<Block7> blocks = new ArrayList<>();
        String hash1;
        String unkownValue1;
        String name1;
        String hash2;
        String unkownValue2;
        String name2;
        String name3;
        String name4;
        for (int i = 0; i < numItems; i++) {

            hash1 = Tools.readHexAsString(this.fileInBytes, atOffset, 0x8);
            unkownValue1 = Tools.readHexAsString(this.fileInBytes, atOffset + 0xC, 0x4);
            name1 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x18);
            hash2 = Tools.readHexAsString(this.fileInBytes, atOffset + 0x20, 0x8);
            unkownValue2 = Tools.readHexAsString(this.fileInBytes, atOffset + 0x2C, 0x4);
            name2 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x38);
            name3 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x48);
            name4 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x58);

            blocks.add(new Block7(hash1, unkownValue1, name1, hash2, unkownValue2, name2, name3, name4));
            atOffset += 0x70;
        }
        for(Block7 block : blocks){
            if(!block.getUnkownValue1().equals("FFFFFFFE") || !block.getName1().equals("")){
                System.out.println("THERE WAS A BLOCK7 WITH A SPECIAL VALUE ---------------------------------------------------------------------");
            }
        }

        return blocks;
    }


    public subBlock readBlock0SubData(int type, int from, int to){
        int atOffset = from;

        int numItems = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
        atOffset += 0x4;



        switch(type){
            case 0:
                return readBlock0_0(numItems, atOffset);
            case 1:
                return readBlock0_1(numItems, atOffset);
            case 2:
                return readBlock0_2(numItems, atOffset);
            case 3:
                return readBlock0_3(numItems, atOffset);

        }

        return null;
    }

    public Block0_0List readBlock0_0(int numItems, int startOffset){
        int atOffset = startOffset;
        ArrayList<Block0_0> blocks = new ArrayList<>();
        String string1;
        String parentName = "";
        String parentHash = "";
        String string2;
        for (int i = 0; i < numItems; i++) {

            string1 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x8);
            string2 = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x20);
            String parent1Index = Tools.readHexAsString(this.fileInBytes, atOffset + 0x10, 0x4);
            if(Tools.isParsable(parent1Index, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parent1Index, 16))){
                    parentName = keyAndName.get(Integer.parseInt(parent1Index, 16));
                    parentHash = keyAndHash.get(Integer.parseInt(parent1Index, 16));
                }
            } else {
                parentName = parent1Index;
                parentHash = parent1Index;
            }


            blocks.add(new Block0_0(string1, parentName, parentHash, string2));
            atOffset += 0x28;
        }




        return new Block0_0List(blocks);
    }

    public Block0_1 readBlock0_1(int numItems, int startOffset){
        int atOffset = startOffset;
        String name = "";
        // map = <name, ArrayList<parentName, parentHash>, String>
        Map<String, ArrayList<Map.Entry<Map.Entry<String, String>, String>>> map = new HashMap<>();
        for (int i = 0; i < numItems; i++) {


            name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x8);


            ArrayList<Map.Entry<Map.Entry<String, String>, String>> strings = new ArrayList<>();

            ArrayList<BlockAdress> blockAdresses = Tools.scanForBlockAdress(this.fileInBytes, atOffset + 0x18, atOffset + 0x30);
            for (BlockAdress ba : blockAdresses) {
                int atSubOffset = ba.getStartPos();
                int numInstances = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                atSubOffset += 0x4;

                for (int j = 0; j < numInstances; j++) {
                    String parentIndex = Tools.readHexAsString(this.fileInBytes, atSubOffset + 0xC, 0x4);
                    String nameInside = Tools.readStringFromOffset(this.fileInBytes, atSubOffset + 0x18);
                    if(Tools.isParsable(parentIndex, 16)) {
                        if(keyAndHash.containsKey(Integer.parseInt(parentIndex, 16))) {
                            strings.add( new AbstractMap.SimpleEntry(
                                    new AbstractMap.SimpleEntry(keyAndName.get(Integer.parseInt(parentIndex, 16)), keyAndHash.get(Integer.parseInt(parentIndex, 16)))
                                    , nameInside
                            ));
                        }
                    }
                    atSubOffset += 0x20;
                }
            }
            map.put(name, strings);
            atOffset += (0x30);

        }

        return new Block0_1();
    }

    public Block0_2 readBlock0_2(int numItems, int startOffset){
        int atOffset = startOffset;
        String name = "";
        String parentName = "";
        String parentHash = "";
        ArrayList<Map.Entry<String, Map.Entry<String, String>>> map = new ArrayList<>();


        for (int i = 0; i < numItems; i++) {
            name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x8);
            String parentIndex = Tools.readHexAsString(this.fileInBytes, atOffset + 0x10, 0x4);
            if(Tools.isParsable(parentIndex, 16)){
                if (keyAndName.containsKey(Integer.parseInt(parentIndex, 16))){
                    parentName = keyAndName.get(Integer.parseInt(parentIndex, 16));
                    parentHash = keyAndHash.get(Integer.parseInt(parentIndex, 16));
                }
            } else {
                parentName = parentIndex;
                parentHash = parentIndex;
            }

            map.add(new AbstractMap.SimpleEntry(name, new AbstractMap.SimpleEntry(parentName, parentHash)));

            atOffset += 0x18;
        }


        return new Block0_2(map);
    }

    public Block0_3 readBlock0_3(int numItems, int startOffset){
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
        return new Block0_3(map);
    }

}
