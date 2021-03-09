package Decoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Tools {

    public static boolean isParsable(String input, int radix) {
        try {
            Integer.parseInt(input, radix);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBIN1File(byte[] fileInBytes){
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

    public static String bytesToString(byte[] bytes){
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String readHexAsString(byte[] bytes, int from, int amount){
        bytes = Arrays.copyOfRange(bytes, from, from + amount);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String byteString = String.format("%02X", b);
            sb.append(new StringBuilder(byteString).reverse().toString());
        }
        sb.reverse();
        return sb.toString();
    }

    public static String readHexAsStringReverse(byte[] bytes, int from, int amount){
        bytes = Arrays.copyOfRange(bytes, from, from + amount);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String byteString = String.format("%02X", b);
            sb.append(new StringBuilder(byteString).toString());
        }
        return sb.toString();
    }

    public static byte[] readBytes(byte[] bytes, int from, int amount){
        return Arrays.copyOfRange(bytes, from, from + amount);
    }

    public static String readString(byte[] bytes, int from, int amount){
        byte[] strInBytes = Arrays.copyOfRange(bytes, from, from + amount);
        String str = new String(strInBytes, 0, strInBytes.length, StandardCharsets.UTF_8);
        return str;
    }

    public static String readStringFromOffset(byte[] bytes, int stringOffsetOffset){
        int atOffset = stringOffsetOffset;

        int strOffset = Integer.parseInt(readHexAsString(bytes, atOffset, 0x4), 16) + 0x10;
        int strLength = Integer.parseInt(readHexAsString(bytes, strOffset - 0x4, 0x4), 16);
        if(strLength > 0) {
            return readString(bytes, strOffset, strLength).substring(0, strLength - 1);
        }
        else return "";
    }

    public static boolean isBlockAdress(String blockAdress, String row1, String row2, String row3){


        if(!row1.toLowerCase().contains("ffffffff")){
            if(row2.equals(row3)){
                return true;
            }
        }
        return false;
    }



    public static ArrayList<BlockAdress> scanForBlockAdress(byte[] fileInBytes, int from, int to){
        int offset = from;
        int scanArea = to - from;

        ArrayList<BlockAdress> blockAdresses = new ArrayList<>();

        for (int i = 0; i < scanArea / 0x18; i++) {
            byte[] adressBlock = Arrays.copyOfRange(fileInBytes, offset, offset + 0x18);
            String adressBlockAsString = Tools.readHexAsString(adressBlock, 0x0, adressBlock.length);

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


}
