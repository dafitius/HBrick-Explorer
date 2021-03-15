package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class ZRuntimeResourceID implements nProperty {

        private int m_IDHigh;
        private int m_IDLow;

    public ZRuntimeResourceID(int m_IDHigh, int m_IDLow) {
        this.m_IDHigh = m_IDHigh;
        this.m_IDLow = m_IDLow;
    }


    @Override
    public String toString() {
        return
                "\"m_IDHigh\": " + m_IDHigh +
                ", \"m_IDLow\": " + m_IDLow;
    }

    public int getM_IDHigh() {
        return m_IDHigh;
    }

    public int getM_IDLow() {
        return m_IDLow;
    }
}
