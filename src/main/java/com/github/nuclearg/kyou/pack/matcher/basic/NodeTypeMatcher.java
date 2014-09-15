package com.github.nuclearg.kyou.pack.matcher.basic;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.dom.KyouArray;
import com.github.nuclearg.kyou.dom.KyouDocument;
import com.github.nuclearg.kyou.dom.KyouField;
import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.dom.KyouStruct;
import com.github.nuclearg.kyou.pack.matcher.Matcher;

/**
 * 匹配元素类型
 * 
 * @author ng
 * 
 */
public class NodeTypeMatcher extends Matcher {
    private final Type type;

    public NodeTypeMatcher(String type) {
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
