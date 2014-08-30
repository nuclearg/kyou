package net.nuclearg.kyou.util;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * Kyou在报文处理过程中处理的值
 * 
 * @author ng
 * 
 */
public class KyouValue {
    /**
     * 类型
     */
    public final KyouValueType type;

    public final KyouItem domValue;
    public final int intValue;
    public final String strValue;
    public final byte[] bytesValue;

    public KyouValue(KyouItem item) {
        this.type = KyouValueType.Dom;

        this.domValue = item;
        this.intValue = 0;
        this.strValue = null;
        this.bytesValue = null;
    }

    public KyouValue(int intValue) {
        this.type = KyouValueType.Integer;

        this.domValue = null;
        this.intValue = intValue;
        this.strValue = null;
        this.bytesValue = null;
    }

    public KyouValue(String strValue) {
        this.type = KyouValueType.String;

        this.domValue = null;
        this.intValue = 0;
        this.strValue = strValue;
        this.bytesValue = null;
    }

    public KyouValue(byte[] bytesValue) {
        this.type = KyouValueType.Bytes;

        this.domValue = null;
        this.intValue = 0;
        this.strValue = null;
        this.bytesValue = bytesValue;
    }

    public KyouValue(KyouValue value) {
        this.type = value.type;

        this.domValue = value.domValue;
        this.intValue = value.intValue;
        this.strValue = value.strValue;
        this.bytesValue = value.bytesValue;
    }
}
