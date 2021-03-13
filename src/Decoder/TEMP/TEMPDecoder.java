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
    File TBLUfile;
    byte[] fileInBytes;
    String hitmanVersion;
    private Map<Long,String> g_propertiesValues = new HashMap<>();

    private int blueprintIndexInResourceHeader;
    private int[] externalSceneTypeIndicesInResourceHeader;
    private ArrayList<SEntityTemplatePropertyOverride> propertyOverrides;
    private int rootEntity;
    private ArrayList<STemplateFactorySubEntity> subEntities;
    private int subType;

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
                String propertyName = this.g_propertiesValues.get(propertyID);
                if(propertyName == null) propertyName = propertyID + "";
                int propertyDataOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x30, 0x4), 16);
                SEntityTemplateProperty property = new SEntityTemplateProperty(PropertyDecoder.readProperty(propertyName, propertyDataOffset, this.fileInBytes));
                System.out.println(propertyName);
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

                ////extract postInitPropertyValues
                if(Tools.isParsable(Tools.readHexAsString(this.fileInBytes, atOffset + 0x40, 0x4), 16)) {
                    int atSubOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atOffset + 0x40, 0x4), 16) + 0xC;
                    int subAmount = Integer.parseUnsignedInt(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                    atSubOffset += 0x4;
                    postInitPropertyValues = new ArrayList<>(subAmount);
                    for (int j = 0; j < subAmount; j++) {

                        long propertyID = Long.parseUnsignedLong(Tools.readHexAsString(this.fileInBytes, atSubOffset, 0x4), 16);
                        String propertyName = this.g_propertiesValues.get(propertyID);
                        int propertyDataOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset + 0x10, 0x4), 16);
                        SEntityTemplateProperty property = new SEntityTemplateProperty(PropertyDecoder.readProperty(propertyName, propertyDataOffset, this.fileInBytes));
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
                        String propertyName = this.g_propertiesValues.get(propertyID);
                        int propertyDataOffset = Integer.parseInt(Tools.readHexAsString(this.fileInBytes, atSubOffset + 0x10, 0x4), 16);
                        SEntityTemplateProperty property = new SEntityTemplateProperty(PropertyDecoder.readProperty(propertyName, propertyDataOffset, this.fileInBytes));
                        propertyValues.add(property);
                        atSubOffset += 0x18;
                    }
                }
                atOffset += 0x58;
                if(hitmanVersion.equals("H3")) atOffset += 0x18;

                TFSEs.add(new STemplateFactorySubEntity(entityTypeResourceIndex, reference, postInitPropertyValues, propertyValues));
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



    public STemplateEntityFactory decode(File file, String hitmanVersion) throws IOException{
        long start = System.currentTimeMillis();
        this.hitmanVersion = hitmanVersion;
        this.TBLUfile = file;
        this.fileInBytes = Files.readAllBytes(Paths.get(file.getPath()));
        this.g_propertiesValues = fillPropertyMap();
        this.blueprintIndexInResourceHeader = readBlueprintIndexInResourceHeader();
        this.externalSceneTypeIndicesInResourceHeader = readExternalSceneTypeIndicesInResourceHeader();
        this.propertyOverrides = readPropertyOverrides();
        this.rootEntity = readRootEntity();
        this.subEntities = readSubEntities();
        this.subType = readSubType();
        STemplateEntityFactory templateEntity = new STemplateEntityFactory(blueprintIndexInResourceHeader, externalSceneTypeIndicesInResourceHeader, propertyOverrides, rootEntity, subEntities, subType);
        //System.out.println(templateEntity);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed);
        return templateEntity;
    }

    public TEMPDecoder() {

    }



}



