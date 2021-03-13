package Decoder.TEMP;

import Decoder.DataTypes.SMatrix34;
import Decoder.DataTypes.SVector3;
import Decoder.TEMP.BlockTypes.SEntityTemplateReference;
import Decoder.TEMP.BlockTypes.nPropertyID;
import Decoder.TEMP.BlockTypes.properties.*;
import Decoder.Tools;

public class PropertyDecoder {

    public static nPropertyID readProperty(String name, int atOffset, byte[] file) {
        if (name != null) {
            switch (name) {
                case "m_aValues":
                    return new m_aValues();
                case "m_mTransform":
                    return readm_mTransform(atOffset, file);
                case "m_eidParent":
                    return readm_eidParent(atOffset, file);
                case "m_RepositoryId":
                    return readm_RepositoryId(atOffset, file);
            }

        }
        return new unknown(name);
    }

    private static nPropertyID readm_mTransform(int atOffset, byte[] file){
        atOffset += 0x10;
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

        return new m_mTransform(new SMatrix34(Trans, xAxis, yAxis, zAxis));
    }

    private static nPropertyID readm_eidParent(int atOffset, byte[] file){
        atOffset += 0x10;
        long entityID = Long.parseUnsignedLong(Tools.readHexAsString(file, atOffset, 0x8), 16);
        int entityIndex = Integer.parseUnsignedInt(Tools.readHexAsString(file, atOffset + 0xC, 0x4), 16);
        String exposedEntity = Tools.readStringFromOffset(file, atOffset + 0x18);
        int externalSceneIndex = Integer.parseUnsignedInt(Tools.readHexAsString(file, atOffset + 0x8, 0x4), 16);
        SEntityTemplateReference reference = new SEntityTemplateReference(entityID, entityIndex, exposedEntity, externalSceneIndex);
        return new m_eidParent(reference);
    }

    private static nPropertyID readm_RepositoryId(int atOffset, byte[] file){
        atOffset += 0x10;
        String part1 = Tools.readHexAsString(file, atOffset, 0x4).toLowerCase();
        String part2 = Tools.readHexAsString(file, atOffset + 0x4, 0x2).toLowerCase();
        String part3 = Tools.readHexAsString(file, atOffset + 0x6, 0x2).toLowerCase();
        String part4 = Tools.readHexAsStringReverse(file, atOffset + 0x8, 0x8).toLowerCase();
        return new m_RepositoryId(part1 + "-" + part2 + "-" + part3 + "-" + part4);
    }
}
