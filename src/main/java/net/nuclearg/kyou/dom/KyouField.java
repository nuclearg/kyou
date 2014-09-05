package net.nuclearg.kyou.dom;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.visitor.KyouDomVisitor;

import org.apache.commons.lang.SystemUtils;

/**
 * 报文结构中的字段
 * 
 * @author ng
 * 
 */
public class KyouField extends KyouItem {
    /**
     * 该报文字段的值
     */
    private String value;

    /** 该报文字段的值 */
    public String value() {
        return this.value;
    }

    /** 该报文字段的值 */
    public void value(String value) {
        if (value == null)
            throw new KyouException("value is null");
        this.value = value;
    }

    @Override
    public void foreach(KyouDomVisitor visitor) {
        if (visitor == null)
            throw new KyouException("foreach visitor is null");
        visitor.field(this);
    }

    @Override
    public String toString() {
        return super.buildToStringPrefix() + "- " + this.name + " = " + this.value + " " + this.attributes + SystemUtils.LINE_SEPARATOR;
    }

    @Override
    public KyouField deepCopy() {
        KyouField copy = this.deepCopyStruct();
        copy.value(this.value);
        return copy;
    }

    @Override
    public KyouField deepCopyStruct() {
        KyouField copy = new KyouField();
        copy.attributes.putAll(this.attributes);
        return copy;
    }

}
