package com.github.nuclearg.kyou.dom.serialize;

import java.io.OutputStream;

import com.github.nuclearg.kyou.dom.KyouDocument;

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
