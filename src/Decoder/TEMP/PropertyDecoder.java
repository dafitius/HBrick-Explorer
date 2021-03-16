package Decoder.TEMP;

import Decoder.DataTypes.*;
import Decoder.TEMP.BlockTypes.SEntityTemplateReference;
import Decoder.TEMP.BlockTypes.nProperty;
import Decoder.Tools;

import java.util.ArrayList;

public class PropertyDecoder {

    public static nProperty readProperty(String dataType, int atOffset, byte[] file, boolean initSkip) {
        if(initSkip) atOffset += 0x10;
        if (dataType != null) {
            switch (dataType) {
                case "SColorRGB":
                    return readSColorRGB(atOffset, file);
                case "SMatrix43":
                    return readSMatrix43(atOffset, file);
                case "SEntityTemplateReference":
                    return readSEntityTemplateReference(atOffset, file);
                case "ZGuid":
                    return readZGuid(atOffset, file);
                case "float32":
                    return readfloat32(atOffset, file);
                case "bool":
                    return readbool(atOffset, file);
                case "ZString":
                    return readZString(atOffset, file);
                case "SVector3":
                    return readSVector3(atOffset, file);
                case "ZRuntimeResourceID":
                    return readResourceRuntimeID(atOffset, file);
                case "int32":
                    return readInt32(atOffset, file);
                case "ZGameTime":
                    return readZGameTime(atOffset, file);
                case "uint32":
                    return readUint32(atOffset, file);
            }

        }
        if(dataType.toLowerCase().startsWith("e") || dataType.toLowerCase().contains(".e")){
            return readEnum(atOffset, file, dataType);
        }
        if(dataType.startsWith("TArray<")){
            String ArraydataType = dataType.replace("TArray<", "").replace(">", "");
            return readArray(atOffset, file, ArraydataType);
        }
        return new unknown(dataType);
    }

    private static nProperty readSMatrix43(int atOffset, byte[] file){

        Long i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x0, 0x4), 16);
        float Xx = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x4, 0x4), 16);
        float Xy = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x8, 0x4), 16);
        float Xz = Float.intBitsToFloat(i.intValue());
        SVector3 xAxis = new SVector3(Xx, Xy, Xz);

        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0xC, 0x4), 16);
        float Yx = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x10, 0x4), 16);
        float Yy = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x14, 0x4), 16);
        float Yz = Float.intBitsToFloat(i.intValue());
        SVector3 yAxis = new SVector3(Yx, Yy, Yz);

        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x18, 0x4), 16);
        float Zx = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x1C, 0x4), 16);
        float Zy = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x20, 0x4), 16);
        float Zz = Float.intBitsToFloat(i.intValue());
        SVector3 zAxis = new SVector3(Zx, Zy, Zz);

        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x24, 0x4), 16);
        float Tx = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x28, 0x4), 16);
        float Ty = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x2C, 0x4), 16);
        float Tz = Float.intBitsToFloat(i.intValue());
        SVector3 Trans = new SVector3(Tx, Ty, Tz);

        return new SMatrix34(Trans, xAxis, yAxis, zAxis);
    }

    private static nProperty readSColorRGB(int atOffset, byte[] file){

        Long i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x0, 0x4), 16);
        float Xx = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x4, 0x4), 16);
        float Xy = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x8, 0x4), 16);
        float Xz = Float.intBitsToFloat(i.intValue());
        return new SColorRGB(Xx, Xy, Xz);

    }

    private static nProperty readSEntityTemplateReference(int atOffset, byte[] file){

        long entityID = Long.parseUnsignedLong(Tools.readHexAsString(file, atOffset, 0x8), 16);
        int entityIndex = Integer.parseUnsignedInt(Tools.readHexAsString(file, atOffset + 0xC, 0x4), 16);
        String exposedEntity = Tools.readStringFromOffset(file, atOffset + 0x18);
        int externalSceneIndex = Integer.parseUnsignedInt(Tools.readHexAsString(file, atOffset + 0x8, 0x4), 16);
        SEntityTemplateReference reference = new SEntityTemplateReference(entityID, entityIndex, exposedEntity, externalSceneIndex);
        return new SEntityTemplateReferenceProperty(reference);
    }

    private static nProperty readZGuid(int atOffset, byte[] file){

        String part1 = Tools.readHexAsString(file, atOffset, 0x4).toLowerCase();
        String part2 = Tools.readHexAsString(file, atOffset + 0x4, 0x2).toLowerCase();
        String part3 = Tools.readHexAsString(file, atOffset + 0x6, 0x2).toLowerCase();
        String part4 = Tools.readHexAsStringReverse(file, atOffset + 0x8, 0x8).toLowerCase();
        return new ZGuid(part1 + "-" + part2 + "-" + part3 + "-" + part4);
    }

    private static nProperty readfloat32(int atOffset, byte[] file){

        Long i = Long.parseLong(Tools.readHexAsString(file, atOffset, 0x4), 16);
        float value = Float.intBitsToFloat(i.intValue());
        return new float32(value);

    }

    private static nProperty readbool(int atOffset, byte[] file){

        int value = Integer.parseInt(Tools.readHexAsString(file, atOffset, 0x4),16);
        if(value == 0)return new bool(false);
        if(value == 1)return new bool(true);
        return new bool();
    }

    private static nProperty readInt32(int atOffset, byte[] file){

        return new int32(Long.parseLong(Tools.readHexAsString(file, atOffset, 0x4), 16));
    }

    private static nProperty readUint32(int atOffset, byte[] file){

        return new uint32(Long.parseLong(Tools.readHexAsString(file, atOffset, 0x4), 16));
    }

    private static nProperty readZGameTime(int atOffset, byte[] file){

        return new ZGameTime(Long.parseLong(Tools.readHexAsString(file, atOffset, 0x4), 16));
    }

    private static nProperty readZString(int atOffset, byte[] file){


        return new ZString(Tools.readStringFromOffset(file, atOffset + 0x8));
    }

    private static nProperty readSVector3(int atOffset, byte[] file){

        Long i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x0, 0x4), 16);
        float Sx = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x4, 0x4), 16);
        float Sy = Float.intBitsToFloat(i.intValue());
        i = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x8, 0x4), 16);
        float Sz = Float.intBitsToFloat(i.intValue());
        return new SVector3(Sx, Sy, Sz);


    }

    private static nProperty readResourceRuntimeID(int atOffset, byte[] file) {

        long high = Long.parseLong(Tools.readHexAsString(file, atOffset, 0x4), 16);
        long low = Long.parseLong(Tools.readHexAsString(file, atOffset + 0x4, 0x4), 16);
        return new ZRuntimeResourceID(high, low);
    }

    private static nProperty readEnum(int atOffset, byte[] file, String dataType){

        int value = Integer.parseInt(Tools.readHexAsString(file, atOffset, 0x4), 16);
        return new enumValue(EnumReader.readEnum(dataType, value));
    }

    private static nProperty readArray(int atOffset, byte[] file, String dataType){
        ArrayList<nProperty> properties = new ArrayList<>();
        if(Tools.isParsable(Tools.readHexAsString(file, atOffset + 0x8, 0x4), 16)) {
            int endOffset = (Integer.parseInt(Tools.readHexAsString(file, atOffset + 0x8, 0x4), 16)) + 0xC;
            int startOffset = (Integer.parseInt(Tools.readHexAsString(file, atOffset, 0x4), 16)) + 0xC;
            atOffset = startOffset;
            long numEntries = Long.parseLong(Tools.readHexAsString(file, atOffset, 0x4), 16);
            if (numEntries != 0) {
                long blockSize = (endOffset - startOffset) / numEntries;
                atOffset += 0x4;
                for (int i = 0; i < numEntries; i++) {

                    properties.add(readProperty(dataType, atOffset, file, false));
                    atOffset += blockSize;
                }
                return new TArray(properties);
            }
        }
        return null;
    }
}
