package net.nuclearg.kyou;

import java.io.InputStream;
import java.io.OutputStream;

import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.serialize.XmlDomSerializer;
import net.nuclearg.kyou.pack.Packer;
import net.nuclearg.kyou.pack.StyleSpecification;

/**
 * kyou的入口类，提供kyou的基本功能
 * <p>
 * 本类是Kyou的入口，可以实现kyou的主要功能：
 * <li>读取/保存{@link KyouDocument}</li>
 * <li>读取{@link StyleSpecification}</li>
 * <li>kyou最根本的存在价值：使用{@link KyouDocument}和{@link StyleSpecification}执行报文组包过程</li>
 * </p>
 * <p>
 * 在load/save系列方法中默认使用{@link XmlDomSerializer}作为序列化/反序列化实现。<br/>
 * 如果期望使用一个不同的序列化策略，请手工调用其它的序列化/反序列化实现。
 * </p>
 * 
 * @author ng
 */
public class Kyou {

    /**
     * 加载一篇报文数据
     * 
     * @param is
     *            输入
     * @return 加载好的报文
     */
    public KyouDocument loadDocument(InputStream is) {
        return new XmlDomSerializer().deserialize(is);
    }

    /**
     * 保存一篇报文数据
     * 
     * @param doc
     * @param os
     */
    public void saveDocument(KyouDocument doc, OutputStream os) {

    }

    /**
     * 加载一篇报文样式定义
     * 
     * @param is
     *            输入
     * @return 加载好的报文样式定义
     */
    public StyleSpecification loadStyleSpecification(InputStream is) {
        return new StyleSpecification(is);
    }

    /**
     * 执行组包过程
     * 
     * @param doc
     *            报文定义
     * @param spec
     *            组包样式定义
     * @return 组包好的报文
     */
    public byte[] pack(KyouDocument doc, StyleSpecification spec) {
        if (doc == null)
            throw new KyouException("doc is null");
        if (spec == null)
            throw new KyouException("spec is null");

        return new Packer().packDocument(doc, spec);
    }
}
