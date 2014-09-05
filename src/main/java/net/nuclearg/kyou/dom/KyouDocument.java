package net.nuclearg.kyou.dom;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.visitor.KyouDomVisitor;

/**
 * 报文数据
 * <p>
 * 报文数据表示一篇完整的报文中的各项细节，包括结构树、每个节点的值和额外的属性
 * </p>
 * 
 * @author ng
 * 
 */
public class KyouDocument extends KyouStruct {

    public KyouDocument() {
        this.name = "#";
    }

    @Override
    public void foreach(KyouDomVisitor visitor) {
        if (visitor == null)
            throw new KyouException("foreach visitor is null");

        visitor.docStart(this);

        super.foreach(visitor);

        visitor.docEnd(this);
    }

    @Override
    public KyouDocument deepCopy() {
        KyouDocument copy = new KyouDocument();
        copy.attributes.putAll(this.attributes);

        for (KyouItem child : this)
            copy.add(child.name, child.deepCopy());

        return copy;
    }

    @Override
    public KyouDocument deepCopyStruct() {
        KyouDocument copy = new KyouDocument();
        copy.attributes.putAll(this.attributes);

        for (KyouItem child : this)
            copy.add(child.name, child.deepCopyStruct());

        return copy;
    }

}
