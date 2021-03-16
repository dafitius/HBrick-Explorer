package Decoder.TEMP;

import java.io.*;
import java.util.Scanner;

public class EnumReader {

    public static String readEnum(String dataType, int value) {
        try {
            BufferedReader Reader =
                    new BufferedReader(new FileReader("src/Logic/resources/enums.txt"));
            //File enumFile = new File("src/Logic/resources/enums.txt");
            //Scanner Reader = new Scanner(enumFile);
            String line = "notNull";
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
