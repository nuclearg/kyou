package net.nuclearg.kyou.pack;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.util.KyouXmlUtils;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * 报文样式定义
 * <p>
 * {@link StyleSpecification}定义了将数据变成实际的二进制的报文的过程。其中定义了若干个{@link StyleItem}(组包样式单元)，描述了针对某些报文元素采用的组包过程。<br/>
 * 需要定义一个针对根元素("#")的{@link StyleItem}，该{@link StyleItem}是整个组包过程的入口点。<br/>
 * 实际的组包过程大致如下：<br/>
 * <li>1. 先找到报文中的根节点</li>
 * <li>2. 由上至下遍历{@link StyleSpecification}，找到第一个与之匹配的{@link StyleItem}</li>
 * <li>3. 使用这个{@link StyleItem}中定义的格式对报文数据进行组包。如果这个{@link StyleItem}引用了其它元素（例如m参数，其表示对自身的各个子节点进行组包）则拿到这些元素，递归重复2和3。</li>
 * </p>
 * 
 * @author ng
 */
public class StyleSpecification {
    /**
     * 全局配置
     */
    public final StyleSpecificationConfig config;
    /**
     * 组包样式单元列表
     */
    final List<StyleItem> styles;
    /**
     * 组包脚本列表
     */
    final List<StyleScript> scripts;

    /**
     * XML的输入流初始化一个StyleSpecification实例
     * 
     * @param is
     *            包含XML的输入流
     */
    public StyleSpecification(InputStream is) {
        try {
            if (is == null)
                throw new KyouException("in is null");

            Document doc = KyouXmlUtils.load(new InputSource(is));

            // 初始化config
            Element config = KyouXmlUtils.selectElement(doc, "/spec/config");
            this.config = new StyleSpecificationConfig(config);

            // 初始化styles
            List<Element> styleElements = KyouXmlUtils.selectElementList(doc, "/spec/style");
            List<StyleItem> styles = new ArrayList<StyleItem>();
            for (Element e : styleElements)
                styles.add(new StyleItem(e, this));
            this.styles = Collections.unmodifiableList(styles);

            // 初始化scripts
            List<Element> scriptElements = KyouXmlUtils.selectElementList(doc, "/spec/script");
            List<StyleScript> scripts = new ArrayList<StyleScript>();
            for (Element e : scriptElements)
                scripts.add(new StyleScript(e));
            this.scripts = Collections.unmodifiableList(scripts);
        } catch (Exception ex) {
            throw new KyouException("load style spec fail. ", ex);
        }
    }

    /**
     * StyleSpecification的全局配置类
     * 
     * @author ng
     */
    public static class StyleSpecificationConfig {
        /**
         * 整篇报文使用的编码
         */
        public final Charset encoding;

        /**
         * 从XML中初始化一个StyleSpecificationConfig实例
         * 
         * @param e
         *            XML节点
         */
        StyleSpecificationConfig(Element e) {
            try {
                String encoding = KyouXmlUtils.selectText(e, "encoding");
                if (StringUtils.isEmpty(encoding))
                    throw new KyouException("style spec encoding is empty");

                this.encoding = Charset.forName(encoding);
            } catch (UnsupportedCharsetException ex) {
                throw new KyouException("unsupported style spec encoding. encoding: " + KyouXmlUtils.selectText(e, "encoding"));
            }
        }
    }
}
