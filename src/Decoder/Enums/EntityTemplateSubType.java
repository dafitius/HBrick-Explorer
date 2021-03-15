package Decoder.Enums;

public class EntityTemplateSubType {

    public static String get(int value){
        switch(value){
            case 0:
                return "entityTemplate";
            case 1:
                return "entity";
            case 2:
                return "brick";
        }
        return value + "";
    }

}
