package net.nuclearg.kyou.pack.match;

import net.nuclearg.kyou.dom.KyouArray;
import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.KyouField;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.dom.KyouStruct;

import org.apache.commons.lang.StringUtils;

/**
 * 匹配元素类型
 * 
 * @author ng
 * 
 */
class TypeMatcher extends Matcher {
    private final Type type;

    TypeMatcher(String type) {
        this.type = Enum.valueOf(Type.class, StringUtils.capitalize(type));
    }

    @Override
    public boolean matches(KyouItem item) {
        switch (this.type) {
            case Document:
                return item instanceof KyouDocument;
            case Field:
                return item instanceof KyouField;
            case Array:
                return item instanceof KyouArray;
            case Struct:
                return item instanceof KyouStruct && !(item instanceof KyouDocument);
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
