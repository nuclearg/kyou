package com.github.nuclearg.kyou.util.value;

import com.github.nuclearg.kyou.dom.KyouItem;

/**
 * Kyou在报文处理过程中处理的值
 * 
 * @author ng
 * 
 */
public class Value {
    /**
     * 类型
     */
    public final ValueType type;

    public final KyouItem domValue;
    public final int intValue;
    public final String strValue;
    public final byte[] bytesValue;

    public Value(KyouItem item) {
        this.type = ValueType.Dom;

        this.domValue = item;
        this.intValue = 0;
        this.strValue = null;
        this.bytesValue = null;
    }

    public Value(int intValue) {
        this.type = ValueType.Integer;

        this.domValue = null;
        this.intValue = intValue;
        this.strValue = null;
        this.bytesValue = null;
    }

    public Value(String strValue) {
        this.type = ValueType.String;

        this.domValue = null;
        this.intValue = 0;
        this.strValue = strValue;
        this.bytesValue = null;
    }

    public Value(byte[] bytesValue) {
        this.type = ValueType.Bytes;

        this.domValue = null;
        this.intValue = 0;
        this.strValue = null;
        this.bytesValue = bytesValue;
    }

    public Value(ValueType type, KyouItem domValue, int intValue, String strValue, byte[] bytesValue) {
        this.type = type;
        this.domValue = domValue;
        this.intValue = intValue;
        this.strValue = strValue;
        this.bytesValue = bytesValue;
    }

    @Override
    public String toString() {
        switch (this.type) {
            case Dom:
                return "[D]" + this.domValue;
            case Bytes:
                return "[B]" + this.bytesValue;
            case Integer:
                return "[I]" + this.intValue;
            case String:
                return "[S]" + this.strValue;
            case Backspace:
                return "[BackSpace]" + this.intValue;
            case Null:
                return "[Null]null";
            case RefParam:
                return "[Ref]" + this.intValue;
            default:
                throw new UnsupportedOperationException("value type " + this.type);
        }
    }
}
