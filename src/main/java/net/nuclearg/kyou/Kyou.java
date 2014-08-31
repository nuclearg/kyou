package net.nuclearg.kyou;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.serialize.XmlDomSerializer;
import net.nuclearg.kyou.pack.KyouPackStyle;
import net.nuclearg.kyou.pack.Packer;

import org.xml.sax.InputSource;

/**
 * kyou的入口类，提供kyou的基本功能
 * <p>
 * 本类是Kyou的入口，可以实现kyou的主要功能：
 * <li>读取/保存{@link KyouDocument}</li>
 * <li>读取{@link KyouPackStyle}</li>
 * <li>kyou最根本的存在价值：使用{@link KyouDocument}和{@link KyouPackStyle}执行报文组包过程</li>
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
     * @return 加载好的报文数据
     */
    public static KyouDocument loadDocument(InputStream is) {
        return new XmlDomSerializer().deserialize(is);
    }

    /**
     * 加载一篇报文数据
     * 
     * @param xml
     *            xml
     * @return 加载好的报文数据
     */
    public static KyouDocument loadDocument(String xml) {
        return new XmlDomSerializer().deserialize(new InputSource(new StringReader(xml)));
    }

    /**
     * 保存一篇报文数据
     * 
     * @param doc
     * @param os
     */
    public static void saveDocument(KyouDocument doc, OutputStream os) {

    }

    /**
     * 加载一篇报文组包样式
     * 
     * @param is
     *            输入
     * @return 加载好的报文组包样式
     */
    public KyouPackStyle loadPackStyle(InputStream is) {
        return new KyouPackStyle(new InputSource(is));
    }

    /**
     * 加载一篇报文组包样式
     * 
     * @param xml
     *            xml文本
     * @return 加载好的报文组包样式
     */
    public static KyouPackStyle loadPackStyle(String xml) {
        return new KyouPackStyle(new InputSource(new StringReader(xml)));
    }

    /**
     * 执行组包过程
     * 
     * @param doc
     *            报文定义
     * @param style
     *            组包样式定义
     * @return 组包好的报文
     */
    public static byte[] pack(KyouDocument doc, KyouPackStyle style) {
        if (doc == null)
            throw new KyouException("doc is null");
        if (style == null)
            throw new KyouException("style is null");

        return new Packer().packDocument(doc, style);
    }
}
