package Decoder.DataTypes;

import Decoder.TEMP.BlockTypes.nProperty;

public class ZRuntimeResourceID implements nProperty {

        private long m_IDHigh;
        private long m_IDLow;

    public ZRuntimeResourceID(long m_IDHigh, long m_IDLow) {
        this.m_IDHigh = m_IDHigh;
        this.m_IDLow = m_IDLow;
    }


    @Override
    public String toString() {
        return
                "\"m_IDHigh\": " + m_IDHigh +
                ", \"m_IDLow\": " + m_IDLow;
    }

    public long getM_IDHigh() {
        return m_IDHigh;
    }

    public long getM_IDLow() {
        return m_IDLow;
    }
}
