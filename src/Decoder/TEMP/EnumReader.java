package Decoder.TEMP;

import java.io.*;
import java.util.Scanner;

public class EnumReader {

    public static String readEnum(String dataType, int value) {
        try {
            BufferedReader Reader =
                    new BufferedReader(new InputStreamReader(EnumReader.class.getResourceAsStream("/Logic/resources/enums.txt")));
            String line;
            while ((line = Reader.readLine()) != null) {
                if(line.startsWith("enum")) {
                    if (line.contains(dataType)) {
                        if (Reader.readLine().equals("{")) {
                            while (line != null && !line.equals("};")) {
                                line = Reader.readLine();
                                if (line.contains("=")) {
                                    String[] enumEntry = line.split("=");
                                    int enumNumber = Integer.parseInt(enumEntry[1]);
                                    if (enumNumber == value) {
                                        return enumEntry[0];
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "enum not found";
    }


}
