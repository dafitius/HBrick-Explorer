package Decoder.TBLU;

import Decoder.TBLU.BlockTypes.*;
import sample.ItemLibrary;
import Decoder.Tools;
import Files.TBLU;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TBLUDecoder {

    File TBLUfile;
    TBLU deserializedFile;
    byte[] fileInBytes;
    HashMap<Integer, String> keyAndName = new HashMap<>();
    HashMap<Integer, String> keyAndHash = new HashMap<>();
    ItemLibrary itemLibrary;


    public TBLUDecoder() {

    }


    public TBLU decode(File file) throws IOException{
        this.TBLUfile = file;
        this.fileInBytes = Files.readAllBytes(Paths.get(file.getPath()));
        System.out.println("start decode");
        if(Tools.isBIN1File(this.fileInBytes)){
            //header size is 0x18, so that will be skipped,
            ArrayList<BlockAdress> blockAdresses = Tools.scanForBlockAdress(this.fileInBytes, 0x18, 0xD8);

            for(BlockAdress ba : blockAdresses){
                readData(ba.getPosition(), ba.getStartPos(), ba.getEndPos());
            }
        }
        return null;
    }


    public void fillParentMaps(int numItems, int atOffset){
        for (int i = 0; i < numItems; i++) {
            String hash = Tools.readHexAsString(this.fileInBytes, atOffset + 0x28, 0x8);
            String name = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x40);
            keyAndHash.put(i, hash);
            keyAndName.put(i, name);
            atOffset += 0xA8;
        }
    }


    public void readData(int type, int from, int to){
            int atOffset = from;
            int numItems = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
            atOffset += 0x4;

        System.out.println("  Type " + type + " found");
        System.out.println("  amount: " + numItems);

        switch(type){
            case 0:
                fillParentMaps(numItems, atOffset);
                readBlock0(numItems, atOffset);
                break;
            case 1:
                readBlock1(numItems, atOffset);
                break;
            case 2:
                readBlock2(numItems, atOffset);
                break;
            case 3:
                readBlock3(numItems, atOffset);
                break;
            case 4:
                readBlock4(numItems, atOffset);
                break;

            case 5:
                readBlock5(numItems, atOffset);
                break;
            case 6:
                System.out.println("BLOCK6 DETECTED  ---------------------------------------------------------------------");
                break;
            case 7:
                readBlock7(numItems, atOffset);
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
        Block0_0 block0_0;
        Block0_1 block0_1;
        Block0_2 block0_2;
        Block0_3 block0_3;


        for (int i = 0; i < numItems; i++) {

            block0_0 = new Block0_0();
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

                if (BlockType == 0) block0_0 = (Block0_0) foundBlock;
                if (BlockType == 1) block0_1 = (Block0_1) foundBlock;
                if (BlockType == 2) block0_2 = (Block0_2) foundBlock;
                if (BlockType == 3) block0_3 = (Block0_3) foundBlock;


            }
            atOffset += (0x18 * 4);


            blocks.add(new Block0(parentName, parentHash, entityType, hash, name, block0_0, block0_1, block0_2, block0_3));


        }
        return blocks;
    }
    public ArrayList<Block1> readBlock1(int numItems, int startOffset){
        int atOffset = startOffset;
        System.out.println("type 1");
        ArrayList<Block1> blocks = new ArrayList<>();
        String parentName;
        String parentHash;
        for (int i = 0; i < numItems; i++) {
            blocks.add(new Block1(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4)));
            atOffset += 0x4;
        }
        System.out.println(blocks.toString());
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
        System.out.println("type 5");
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
        System.out.println("type 7");

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
        System.out.println("  sub-Type " + type + " found");
        System.out.println("  amount: " + numItems);


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

    public Block0_0 readBlock0_0(int numItems, int startOffset){
        int atOffset = startOffset;
        return new Block0_0();
    }

    public Block0_1 readBlock0_1(int numItems, int startOffset){
        int atOffset = startOffset;
        return new Block0_1();
    }

    public Block0_2 readBlock0_2(int numItems, int startOffset){
        int atOffset = startOffset;
        return new Block0_2();
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
                System.out.println(numInstances + " " + name + " found");
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
