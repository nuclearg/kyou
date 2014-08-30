package net.nuclearg.kyou.dom.serialize;

import java.io.InputStream;
import java.io.OutputStream;

import net.nuclearg.kyou.dom.KyouDocument;

/**
 * 报文文档的序列化与反序列化接口
 * 
 * @author ng
 * 
 */
public interface DomSerializer {
    /**
     * 序列化
     * 
     * @param doc
     *            报文文档
     * @param os
     *            输出流
     */
    public void serialize(KyouDocument doc, OutputStream os);

    /**
     * 反序列化
     * 
     * @param is
     *            输入流
     * @return 解析出的报文文档
     */
    public KyouDocument deserialize(InputStream is);
}
