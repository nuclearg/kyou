package net.nuclearg.kyou.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.visitor.KyouDomVisitor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * 报文结构中的结构体
 * 
 * @author ng
 * 
 */
public class KyouStruct extends KyouContainer {
    /**
     * 实际存放元素的map
     */
    private final Map<String, KyouItem> children = new LinkedHashMap<String, KyouItem>();

    /**
     * 缓存名称顺序的map
     */
    private final List<String> nameList = new ArrayList<String>();

    /**
     * 向结构体中添加元素
     * 
     * @param item
     *            新元素
     */
    public void add(String name, KyouItem item) {
        if (StringUtils.isBlank(name))
            throw new KyouException("name is blank");
        if (!StringUtils.containsOnly(name, "_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
                || "1234567890".indexOf(name.charAt(0)) != -1)
            throw new KyouException("name illegal");
        if (item == null)
            throw new KyouException("try add null as child");
        if (item == this)
            throw new KyouException("try add self as child");
        if (item.isAncestorOf(this))
            throw new KyouException("try add ancestor as child");
        if (this.nameList.contains(name))
            throw new KyouException("children name duplicate. name: " + name);

        item.name = name;
        item.parent = this;
        this.children.put(name, item);
        this.nameList.add(name);
    }

    @Override
    public Iterator<KyouItem> iterator() {
        return this.children.values().iterator();
    }

    @Override
    public KyouItem get(int index) {
        if (index < 0)
            throw new KyouException("index < 0");
        if (index >= this.size())
            throw new KyouException("index out of size");

        return this.children.get(this.nameList.get(index));
    }

    @Override
    public KyouItem get(String name) {
        if (StringUtils.isEmpty(name))
            throw new KyouException("name is empty");

        return this.children.get(name);
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

        return this.nameList.indexOf(item.name);
    }

    @Override
    public KyouItem remove(int index) {
        if (index < 0)
            throw new KyouException("index < 0");
        if (index >= this.size())
            throw new KyouException("index out of size");

        String name = this.nameList.remove(index);
        KyouItem item = this.children.remove(name);
        item.name = null;
        item.parent = null;

        return item;
    }

    @Override
    public KyouItem remove(String name) {
        if (StringUtils.isEmpty(name))
            throw new KyouException("name is empty");

        int index = this.nameList.indexOf(name);
        if (index < 0)
            return null;

        this.nameList.remove(index);
        KyouItem item = this.children.remove(name);
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

        this.nameList.remove(item.name);
        this.children.remove(item.name);
        item.name = null;
        item.parent = null;

        return true;
    }

    @Override
    public void foreach(KyouDomVisitor visitor) {
        if (visitor == null)
            throw new KyouException("foreach visitor is null");

        visitor.struStart(this);

        for (KyouItem item : this)
            item.foreach(visitor);

        visitor.struEnd(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.buildToStringPrefix()).append("+ ").append(this.name).append(" ").append(this.attributes).append(SystemUtils.LINE_SEPARATOR);

        for (KyouItem item : this)
            builder.append(item.toString());

        return builder.toString();
    }

    @Override
    public KyouStruct deepCopy() {
        KyouStruct copy = new KyouStruct();
        copy.attributes.putAll(this.attributes);

        for (KyouItem child : this)
            copy.add(child.name, child.deepCopy());

        return copy;
    }

    @Override
    public KyouStruct deepCopyStruct() {
        KyouStruct copy = new KyouStruct();
        copy.attributes.putAll(this.attributes);

        for (KyouItem child : this)
            copy.add(child.name, child.deepCopyStruct());

        return copy;
    }

}
