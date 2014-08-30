package net.nuclearg.kyou.pack;

import java.util.List;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.query.KyouQuery;
import net.nuclearg.kyou.util.KyouXmlUtils;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

/**
 * 报文组包样式单元
 * <p>
 * 组包样式单元描述了对于某一类元素的组包方式
 * </p>
 * 
 * @author ng
 */
class StyleItem {
    /**
     * 该组包样式单元隶属于的组包样式定义
     */
    public final StyleSpecification spec;

    /**
     * 该组包样式单元适用于的元素
     */
    final KyouQuery target;
    /**
     * 段列表
     */
    final List<PackSegment> segments;

    /**
     * 从XML中初始化一个StyleItem实例
     * 
     * @param e
     *            XML节点
     * @param spec
     *            该组包样式单元隶属于的组包样式定义
     */
    StyleItem(Element e, StyleSpecification spec) {
        this.spec = spec;

        /*
         * <pre>
         * <style target="...">
         * <format><![CDATA[<%>%</%>]]></format>
         * <param>b2s n</s>
         * <param>m</s>
         * <param>b2s n</s>
         * </style>
         * </pre>
         */

        // 初始化target
        String target = KyouXmlUtils.selectText(e, "@target");
        if (StringUtils.isEmpty(target))
            throw new KyouException("targat is empty");
        this.target = new KyouQuery(target);

        // 读取format
        String format = KyouXmlUtils.selectText(e, "format");
        if (StringUtils.isEmpty(format))
            throw new KyouException("format is empty. target: " + target);

        // 读取用户定义的参数
        List<String> params = KyouXmlUtils.selectTextList(e, "param");

        // 初始化segments
        this.segments = PackSegment.parseFormatString(format, spec.config.encoding, params);
    }
}
