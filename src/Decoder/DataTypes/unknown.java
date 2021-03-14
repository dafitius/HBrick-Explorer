package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class unknown implements nProperty {

    String name = "this property's";

    public unknown(String name) {
        if(name != null) {
            this.name = name;
        } else name = "this property";
    }

    @Override
    public String toString() {
        return name + " decoding hasn't been implemented yet";
    }

}
