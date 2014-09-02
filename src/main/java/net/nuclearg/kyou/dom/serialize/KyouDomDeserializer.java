package net.nuclearg.kyou.dom.serialize;

import java.io.InputStream;

import net.nuclearg.kyou.dom.KyouDocument;

/**
 * 报文文档的反序列化接口
 * 
 * @author ng
 * 
 */
public interface KyouDomDeserializer {
    /**
     * 反序列化
     * 
     * @param is
     *            输入流
     * @return 解析出的报文文档
     */
    public KyouDocument deserialize(InputStream is);
}
