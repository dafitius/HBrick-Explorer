package Decoder.TEMP;

import Decoder.TEMP.BlockTypes.*;
import Decoder.Tools;
import Files.STemplateEntityFactory;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TEMPDecoder {
    File TEMPfile;
    byte[] fileInBytes;
    String hitmanVersion;
    private Map<Long,String> g_propertiesValues = new HashMap<>();

    private int blueprintIndexInResourceHeader;
    private int[] externalSceneTypeIndicesInResourceHeader;
    private ArrayList<SEntityTemplatePropertyOverride> propertyOverrides;
    private int rootEntity;
    private ArrayList<STemplateFactorySubEntity> subEntities;
    private int subType;
    private ArrayList<String> CC_dataTypes;

    private Map<Long,String> fillPropertyMap(){
        Map<Long,String> map = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Logic/resources/g_properties.txt")));
            String line = reader.readLine();
            while (line != null) {
                line = line.split("\\(")[1];
                line = line.replaceAll("\"", "");
                line = line.replaceAll(" ", "");
                line = line.replaceAll("\\);", "");
                String[] parts = line.split(",");
                map.put(Long.parseUnsignedLong(parts[1]), parts[0]);

                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private int readBlueprintIndexInResourceHeader(){
        System.out.println("reading BlueprintIndexInResourceHeader");
        if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, 0x14, 0x4), 16)) {
            int index = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, 0x14, 0x4), 16);
            return index;
        } else {
            System.out.println("NOTHING WAS FOUND THAT IS BAD RIGHT?");
            return -1;
        }
    }

    private int[] readExternalSceneTypeIndicesInResourceHeader(){
        System.out.println("reading ExternalSceneTypeIndicesInResourceHeader");
        if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, 0x50, 0x4), 16)) {
            int atOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, 0x50, 0x4), 16) + 0xC;
            int amount = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
            atOffset += 0x4;
            int[] indexes = new int[amount];
            for (int i = 0; i < amount; i++) {
                indexes[i] = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
                atOffset += 0x4;
            }
            return indexes;
        }
        return null;
    }

    private ArrayList<SEntityTemplatePropertyOverride> readPropertyOverrides(){
        System.out.println("reading PropertyOverrides");
        if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, 0x38, 0x4), 16)) {
            ArrayList<SEntityTemplatePropertyOverride> ETPOs = new ArrayList<>();

            int atOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, 0x38, 0x4), 16) + 0xC;
            int amount = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
            atOffset += 0x4;


            for (int i = 0; i < amount; i++) {
                ////extract SEntityTemplateReference
                long entityID = Long.parseUnsignedLong(Tools.readHexAsString(this.fileInBytes, atOffset, 0x8), 16);
                int entityIndex = Integer.parseUnsignedInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0xC, 0x4), 16);
                String exposedEntity = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x18);
                int externalSceneIndex = Integer.parseUnsignedInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x8, 0x4), 16);
                SEntityTemplateReference reference = new SEntityTemplateReference(entityID, entityIndex, exposedEntity, externalSceneIndex);

                ////extract SEntityTemplateProperty
                long propertyID = Long.parseUnsignedLong(Tools.readHexAsString(this.fileInBytes, atOffset + 0x20, 0x4), 16);
                String propertyName = propertyID + "";
                if(this.g_propertiesValues.containsKey(propertyID)) propertyName = this.g_propertiesValues.get(propertyID);
                int propertyIndex =  Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x28, 0x4), 16);
                int propertyDataOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x30, 0x4), 16);
                SEntityTemplateProperty property = new SEntityTemplateProperty(propertyName, CC_dataTypes.get(propertyIndex), PropertyDecoder.readProperty(CC_dataTypes.get(propertyIndex), propertyDataOffset, this.fileInBytes, true));
                atOffset += 0x38;

                ETPOs.add(new SEntityTemplatePropertyOverride(reference, property));
            }
            return ETPOs;
        }
        else return null;
    }

    private int readRootEntity(){
        System.out.println("reading RootEntity Index");
        if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, 0x18, 0x4), 16)) {
            int index = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, 0x18, 0x4), 16);
            return index;
        } else {
            System.out.println("NOTHING WAS FOUND THAT IS BAD RIGHT?");
            return -1;
        }
    }

    private ArrayList<STemplateFactorySubEntity> readSubEntities(){
        System.out.println("reading SubEntities");
        if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, 0x20, 0x4), 16)) {
            ArrayList<STemplateFactorySubEntity> TFSEs = new ArrayList<>();

            ArrayList<SEntityTemplateProperty> postInitPropertyValues = new ArrayList<>();
            ArrayList<SEntityTemplateProperty> propertyValues = new ArrayList<>();




            int atOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, 0x20, 0x4), 16) + 0xC;
            int amount = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset, 0x4), 16);
            atOffset += 0x4;


            for (int i = 0; i < amount; i++) {
                ////extract SEntityTemplateReference
                int entityTypeResourceIndex = Integer.parseUnsignedInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x20, 0x4), 16);
                long entityID = Long.parseUnsignedLong(Tools.readHexAsString(this.fileInBytes, atOffset, 0x8), 16);
                int entityIndex = Integer.parseUnsignedInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0xC, 0x4), 16);
                String exposedEntity = Tools.readStringFromOffset(this.fileInBytes, atOffset + 0x18);
                int externalSceneIndex = Integer.parseUnsignedInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x8, 0x4), 16);
                SEntityTemplateReference reference = new SEntityTemplateReference(entityID, entityIndex, exposedEntity, externalSceneIndex);
                postInitPropertyValues = new ArrayList<>();
                propertyValues = new ArrayList<>();
                ////extract postInitPropertyValues
                if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, atOffset + 0x40, 0x4), 16)) {
                    int atSubOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x40, 0x4), 16) + 0xC;
                    int subAmount = Integer.parseUnsignedInt(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                    atSubOffset += 0x4;
                    postInitPropertyValues = new ArrayList<>(subAmount);
                    for (int j = 0; j < subAmount; j++) {


                        long propertyID = Long.parseUnsignedLong(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                        String propertyName = propertyID + "";
                        if(this.g_propertiesValues.containsKey(propertyID)) propertyName = this.g_propertiesValues.get(propertyID);
                        int propertyDataOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset + 0x10, 0x4), 16);
                        int propertyIndex =  Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset + 0x8, 0x4), 16);
                        SEntityTemplateProperty property = new SEntityTemplateProperty(propertyName, CC_dataTypes.get(propertyIndex), PropertyDecoder.readProperty(CC_dataTypes.get(propertyIndex), propertyDataOffset, this.fileInBytes, true));
                        postInitPropertyValues.add(property);
                        atSubOffset += 0x18;
                    }
                }
                ////extract PropertyValues
                if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, atOffset + 0x28, 0x4), 16)) {
                    int atSubOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x28, 0x4), 16) + 0xC;
                    int subAmount = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                    atSubOffset += 0x4;
                    propertyValues = new ArrayList<>(subAmount);

                    for (int j = 0; j < subAmount; j++) {
                        long propertyID = Long.parseUnsignedLong(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                        String propertyName = propertyID + "";
                        if(this.g_propertiesValues.containsKey(propertyID)) propertyName = this.g_propertiesValues.get(propertyID);
                        int propertyIndex =  Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset + 0x8, 0x4), 16);
                        int propertyDataOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset + 0x10, 0x4), 16);
                        SEntityTemplateProperty property = new SEntityTemplateProperty(propertyName, CC_dataTypes.get(propertyIndex), PropertyDecoder.readProperty(CC_dataTypes.get(propertyIndex), propertyDataOffset, this.fileInBytes, true));
                        propertyValues.add(property);
                        atSubOffset += 0x18;
                    }
                }
                atOffset += 0x58;
                if(hitmanVersion.equals("H3")) atOffset += 0x18;
                STemplateFactorySubEntity entity = new STemplateFactorySubEntity(entityTypeResourceIndex, reference, postInitPropertyValues, propertyValues);
                entity.setCC_index(i);
                TFSEs.add(entity);
            }
            return TFSEs;

        }
        return null;
    }

    private int readSubType(){
        System.out.println("reading SubType");
        if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, 0x10, 0x4), 16)) {
            int index = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, 0x10, 0x4), 16);
            return index;
        } else {
            System.out.println("NOTHING WAS FOUND THAT IS BAD RIGHT?");
            return -1;
        }
    }

    private ArrayList<String> getDataTypes(){
        ArrayList<String> types = new ArrayList<>();
        int fileSize = Integer.parseInt(Tools.readHexAsStringReverse(this.fileInBytes, 0x8, 0x4), 16);
        fileSize += 0x10;
        fileSize += 0x4;
        fileSize += (Integer.parseInt(Tools.readHexAsString(this.fileInBytes, fileSize + 0x4, 0x4), 16)) * 4;
        fileSize += 0x8;
        fileSize += 0x4;
        fileSize += 0x8;
        if(this.fileInBytes.length > fileSize) {


            if(Tools.readHexAsString(this.fileInBytes, fileSize - 0xc, 0x4).equals("3989BF9F")) {
                int value = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, fileSize, 0x4), 16);

                while (value < Integer.parseInt(Tools.readHexAsString(this.fileInBytes, fileSize + 0x4, 0x4), 16)) {
                    fileSize += 0x4;
                }
                fileSize += 0x4;
                System.out.println(Integer.toHexString(fileSize));
                int amountOfTypes = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, fileSize, 0x4), 16);
                fileSize += 0x4;
                int atOffset = fileSize;
                for (int i = 0; i < amountOfTypes; i++) {
                    int stringLength = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x8, 0x4), 16) - 1;
                    types.add(Tools.readString(this.fileInBytes, atOffset + 0xc, stringLength));
                    atOffset += 0xC;
                    atOffset += stringLength;
                    if (stringLength % 4 == 0) {
                        atOffset += 0x4;
                    } else {
                        int padding = stringLength;
                        while (padding % 4 != 0) {
                            atOffset++;
                            padding++;
                        }
                    }
                }
                System.out.println(types);
            }
        }
        return types;

    }

    private void checkForDependencies(STemplateEntityFactory TEMP) throws IOException {
        File metaFile = new File(this.TEMPfile.getPath() + ".meta");

        if(metaFile.exists()){
            byte[] metaFileinBytes = Files.readAllBytes(Paths.get(metaFile.getPath()));
            int amountOfDependencies = Integer.parseInt(Tools.readHexAsString(metaFileinBytes, 0x2c, 0x3 ), 16);
            int atOffset = 0x30;
            atOffset += amountOfDependencies;
            for (int i = 0; i < amountOfDependencies; i++) {
                TEMP.addDependency(Tools.readHexAsString(metaFileinBytes, atOffset, 0x8));
                atOffset += 0x8;
            }
        }
    }


    public STemplateEntityFactory decode(File file, String hitmanVersion) throws IOException{
        long start = System.currentTimeMillis();
        this.hitmanVersion = hitmanVersion;
        this.TEMPfile = file;
        this.fileInBytes = Files.readAllBytes(Paths.get(file.getPath()));
        this.CC_dataTypes = getDataTypes();
        this.g_propertiesValues = fillPropertyMap();
        this.blueprintIndexInResourceHeader = readBlueprintIndexInResourceHeader();
        this.externalSceneTypeIndicesInResourceHeader = readExternalSceneTypeIndicesInResourceHeader();
        this.propertyOverrides = readPropertyOverrides();
        this.rootEntity = readRootEntity();
        this.subEntities = readSubEntities();
        this.subType = readSubType();

        STemplateEntityFactory templateEntity = new STemplateEntityFactory(blueprintIndexInResourceHeader, externalSceneTypeIndicesInResourceHeader, propertyOverrides, rootEntity, subEntities, subType, CC_dataTypes);
        checkForDependencies(templateEntity);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed);
        return templateEntity;
    }

    public TEMPDecoder() {

    }



}



