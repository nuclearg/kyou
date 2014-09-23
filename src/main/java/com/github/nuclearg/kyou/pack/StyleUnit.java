package com.github.nuclearg.kyou.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.StyleSegment.ParamSegment;
import com.github.nuclearg.kyou.pack.matcher.Matcher;
import com.github.nuclearg.kyou.util.XmlUtils;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 报文组包样式单元
 * <p>
 * 组包样式单元描述了对于某一类元素的组包方式
 * </p>
 * 
 * @author ng
 */
public class StyleUnit {
    /**
     * 该组包样式单元隶属于的组包样式
     */
    public final KyouPackStyle style;

    /**
     * 该组包样式单元适用于的元素
     */
    final Matcher matcher;
    /**
     * 段列表
     */
    final List<StyleSegment> segments;
    /**
     * 参数段列表计数
     */
    final int paramSegmentCount;
    /**
     * 表达式链的列表
     */
    final List<StyleParam> params;

    StyleUnit(Element e, KyouPackStyle style) {
        this.style = style;

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
        String matchStr = XmlUtils.selectText(e, "@match");
        if (StringUtils.isEmpty(matchStr))
            throw new KyouException("match is empty");
        try {
            this.matcher = Matcher.parse(matchStr);
        } catch (Exception ex) {
            throw new KyouException("target syntax error. target: " + matchStr, ex);
        }

        // 读取format
        String formatStr = XmlUtils.selectText(e, "format");

        // 读取用户定义的参数
        List<String> paramStrs = XmlUtils.selectTextList(e, "param");
        List<StyleParam> params = new ArrayList<>();
        for (String paramStr : paramStrs)
            try {
                params.add(new StyleParam(paramStr));
            } catch (Exception ex) {
                throw new KyouException("parse param fail. param: " + paramStr, ex);
            }
        this.params = Collections.unmodifiableList(params);

        // 初始化segments
        try {
            this.segments = StyleSegment.parseFormatString(formatStr, style.config.encoding, this.params);

            int paramSegmentCount = 0;
            for (StyleSegment segment : segments)
                if (segment instanceof ParamSegment)
                    paramSegmentCount++;
            this.paramSegmentCount = paramSegmentCount;
        } catch (Exception ex) {
            throw new KyouException("expr syntax error.  target: " + matchStr, ex);
        }

        // 检查一下，看自身有没有问题
        for (int i = 0; i < this.params.size(); i++) {
            StyleParam param = this.params.get(i);

            // 检查表达式前后衔接
            param.check(this);

            // 检查表达式的输出类型
            // TODO
            // throw new KyouException("last expr must return Bytes or Backspace. paramId: " + i + ", type: " + param.typeOut());
        }
    }

    /**
     * 获取指定参数的类型
     * 
     * @param paramIndex
     *            参数下标
     * @return
     */
    public ValueType getParamType(int paramIndex) {
        // TODO 递归检测

        if (paramIndex >= this.params.size())
            throw new KyouException("ref param index > param count. ref: " + paramIndex + ", count: " + this.params.size());

        StyleParam param = this.params.get(paramIndex);
        ValueType type = param.typeOut();
        switch (type) {
            case Dom:
            case Bytes:
            case Integer:
            case String:
            case Backspace:
                return type;
            case Null:
            case RefParam:
                throw new KyouException("cannot ref param. type not supported. targetId: " + paramIndex + ", targetType: " + type);
            default:
                throw new UnsupportedOperationException("value type " + type);
        }
    }
}
