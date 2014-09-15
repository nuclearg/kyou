package com.github.nuclearg.kyou.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.dom.visitor.KyouDomVisitor;

/**
 * 报文结构中的数组
 * 
 * @author ng
 * 
 */
public class KyouArray extends KyouContainer {
    /**
     * 实际存放元素的list
     */
    private final List<KyouItem> children;
    /**
     * 原型
     */
    private final KyouItem prototype;

    public KyouArray(KyouItem prototype) {
        if (prototype == null)
            throw new KyouException("prototype is null");
        if (prototype instanceof KyouDocument)
            throw new KyouException("document as prototype is not supported");

        prototype.name = "__prototype";
        prototype.parent = null;

        this.children = new ArrayList<>();
        this.prototype = prototype;
    }

    /**
     * 当前数组的原型
     */
    public KyouItem prototype() {
        return this.prototype;
    }

    /**
     * 向数组中添加一个元素
     * <p>
     * 该元素的结构将直接从prototype复制出来
     * </p>
     * 
     * @return 创建出来的元素
     */
    public KyouItem add() {
        KyouItem item = this.prototype.deepCopyStruct();

        int index = this.children.size();
        item.name = String.valueOf(index);
        item.parent = this;

        this.children.add(item);

        return item;
    }

    @Override
    public Iterator<KyouItem> iterator() {
        return this.children.iterator();
    }

    @Override
    public KyouItem get(int index) {
        if (index < 0)
            throw new KyouException("index < 0");
        if (index >= this.size())
            throw new KyouException("index out of size");

        return this.children.get(index);
    }

    @Override
    public KyouItem get(String name) {
        if (StringUtils.isEmpty(name))
            throw new KyouException("name is empty");
        if (!StringUtils.isNumeric(name))
            throw new KyouException("name illegal");

        int index = NumberUtils.toInt(name);
        return this.get(index);
    }

    @Override
    public int size() {
        return this.children.size();
    }

    @Override
    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public int indexOf(KyouItem item) {
        if (item == null)
            return -1;
        if (item.parent != this)
            return -1;

        return this.children.indexOf(item);
    }

    @Override
    public KyouItem remove(int index) {
        if (index < 0)
            throw new KyouException("index < 0");
        if (index >= this.size())
            throw new KyouException("index out of size");

        KyouItem item = this.children.remove(index);
        item.name = null;
        item.parent = null;

        return item;
    }

    @Override
    public KyouItem remove(String name) {
        if (StringUtils.isEmpty(name))
            throw new KyouException("name is empty");
        if (!StringUtils.isNumeric(name))
            throw new KyouException("name illegal");

        int index = NumberUtils.toInt(name);

        KyouItem item = this.remove(index);
        item.name = null;
        item.parent = null;

        return item;
    }

    @Override
    public boolean remove(KyouItem item) {
        if (item == null)
            return false;
        if (item.parent != this)
            return false;

        if (!this.children.remove(item))
            return false;

        item.name = null;
        item.parent = null;
        return true;
    }

    @Override
    public void foreach(KyouDomVisitor visitor) {
        if (visitor == null)
            throw new KyouException("foreach visitor is null");

        visitor.arrayStart(this);

        for (KyouItem item : this)
            item.foreach(visitor);

        visitor.arrayEnd(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.buildToStringPrefix()).append("[]").append(this.name).append(" ").append(this.attributes).append(SystemUtils.LINE_SEPARATOR);

        for (KyouItem item : this)
            builder.append(item.toString());

        return builder.toString();
    }

    @Override
    public KyouItem deepCopy() {
        KyouArray copy = this.deepCopyStruct();

        for (KyouItem item : this)
            copy.children.add(item.deepCopy());

        return copy;
    }

    @Override
    public KyouArray deepCopyStruct() {
        KyouArray copy = new KyouArray(this.prototype.deepCopyStruct());
        copy.attributes.putAll(this.attributes);
        return copy;
    }

}
