package Decoder;

public class Tools {

    public static boolean isParsable(String input, int radix) {
        try {
            Integer.parseInt(input, radix);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

}
