package net.nuclearg.kyou.dom.query;

import net.nuclearg.kyou.dom.KyouArray;
import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.KyouField;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.dom.KyouStruct;

import org.apache.commons.lang.StringUtils;

/**
 * 基于元素类型进行查询
 * 
 * @author ng
 * 
 */
class TypeQueryImpl extends QueryImpl {
    private final Type type;

    TypeQueryImpl(String type) {
        this.type = Enum.valueOf(Type.class, StringUtils.capitalize(type));
    }

    @Override
    boolean matches(KyouItem item) {
        switch (this.type) {
            case Document:
                return item instanceof KyouDocument;
            case Field:
                return item instanceof KyouField;
            case Array:
                return item instanceof KyouArray;
            case Struct:
                return item instanceof KyouStruct;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return this.type.toString().toLowerCase();
    }

    private static enum Type {
        Document,
        Field,
        Array,
        Struct
    }
}
