package net.nuclearg.kyou.dom.serialize;

import java.io.OutputStream;

import net.nuclearg.kyou.dom.KyouDocument;

/**
 * 报文文档的序列化接口
 * 
 * @author ng
 * 
 */
public interface KyouDomSerializer {
    /**
     * 序列化
     * 
     * @param doc
     *            报文文档
     * @param os
     *            输出流
     */
    public void serialize(KyouDocument doc, OutputStream os);
}
